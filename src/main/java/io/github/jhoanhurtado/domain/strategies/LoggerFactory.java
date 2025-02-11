package io.github.jhoanhurtado.domain.strategies;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import io.github.jhoanhurtado.domain.models.AwsConnectionModel;
import io.github.jhoanhurtado.impl.CloudWatchLogger;
import io.github.jhoanhurtado.impl.CompositeLogger;
import io.github.jhoanhurtado.impl.FileLogger;
import io.github.jhoanhurtado.interfaces.Logger;

/**
 * Fábrica singleton para crear y gestionar un {@link CompositeLogger}.
 *
 * Esta clase proporciona un único punto de acceso para crear una instancia de
 * {@link CompositeLogger}, que permite registrar logs tanto en un archivo local
 * como en AWS CloudWatch (si se proporciona una configuración de AWS).
 *
 * La inicialización de la instancia se realiza de manera perezosa y es segura
 * para entornos multi-hilo.
 */
public class LoggerFactory {

    private static Logger compositeLogger;
    private static final Object lock = new Object();
    /**
     * Constructor privado para evitar la instanciación de la clase.
     */

    /**
     * Devuelve una única instancia de {@link CompositeLogger}, inicializándola
     * si es necesario.
     *
     * @param pathString Ruta del archivo de logs. Si es null o vacío, se
     * utiliza una ruta predeterminada en el directorio
     * "Documentos/logs/queue-helper".
     * @param awsConnectionModel Configuración opcional para CloudWatch. Si es
     * null, solo se utilizará el archivo local.
     * @return Instancia única de {@link CompositeLogger}.
     */
    public Logger getCompositeLogger() {
        String pathString = loadLogPathFromProperties("filelog.location");
        boolean localLogsEnable = Boolean.parseBoolean(loadLogPathFromProperties("filelog.enable"));
        boolean cloudLogEnable = Boolean.parseBoolean(loadLogPathFromProperties("cloudwatch.enable"));
        AwsConnectionModel awsConnectionModel = new AwsConnectionModel();
        if (compositeLogger == null) {
            synchronized (lock) {
                // Crear los loggers
                List<Logger> loggers = new ArrayList<>();
                if (compositeLogger == null) {
                    // Determinar la ruta del archivo de logs
                    if (localLogsEnable) {
                        Path defaultLogPath = Paths.get(System.getProperty("user.home"), "Documents", "logs", "queue-helper", "queue-helper.log");
                        Path logFilePath = (pathString == null || pathString.isEmpty() || pathString.equals("")) ? defaultLogPath : Paths.get(pathString);

                        // Asegurar que las carpetas existen
                        ensureDirectoriesExist(logFilePath.getParent());
                        loggers.add(new FileLogger(logFilePath.toString()));
                    }

                    if (isValidAwsConfig(awsConnectionModel) && cloudLogEnable) {
                        loggers.add(new CloudWatchLogger(
                                awsConnectionModel.getAwsRegion().getId(),
                                awsConnectionModel.getLogGroupName(),
                                awsConnectionModel.getLogStreamName()
                        ));
                    }
                    // Crear el CompositeLogger
                    compositeLogger = new CompositeLogger(loggers);
                }
            }
        }
        return compositeLogger;
    }

    private String loadLogPathFromProperties(String properti) {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("local.properties")) {
            properties.load(inputStream);
            return properties.getProperty(properti, ""); // Retorna vacío si no se encuentra la propiedad
        } catch (IOException e) {
            System.err.println("Error al cargar local.properties: " + e.getMessage());
            return "";
        }
    }

    /**
     * Crea las carpetas necesarias para almacenar el archivo de logs si no
     * existen.
     *
     * @param directoryPath Ruta del directorio que debe ser creado.
     */
    private static void ensureDirectoriesExist(Path directoryPath) {
        if (directoryPath != null) {
            try {
                Files.createDirectories(directoryPath);
                // Log cuando se crean las carpetas
            } catch (IOException e) {
                throw new RuntimeException("No se pudo crear el directorio de logs: " + directoryPath, e);
            }
        }
    }

    /**
     * Verifica si la configuración de AWS CloudWatch es válida.
     *
     * @param awsConfig Configuración de AWS a validar.
     * @return true si la configuración es válida, false en caso contrario.
     */
    private static boolean isValidAwsConfig(AwsConnectionModel awsConfig) {
        return awsConfig != null
                && awsConfig.getAwsRegion() != null
                && awsConfig.getLogGroupName() != null && !awsConfig.getLogGroupName().isEmpty()
                && awsConfig.getLogStreamName() != null && !awsConfig.getLogStreamName().isEmpty();
    }
}
