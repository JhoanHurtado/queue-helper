package io.github.jhoanhurtado.infrastructure.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import io.github.jhoanhurtado.enums.AwsRegion;

/**
 * Gestor de configuración para la gestión de logs en archivo local y en AWS
 * CloudWatch.
 * <p>
 * Este gestor permite cargar, modificar y guardar configuraciones desde un
 * archivo "local.properties" ubicado en la carpeta de recursos del proyecto.
 * Garantiza que si el log de CloudWatch no está habilitado, el log de archivo
 * local no pueda desactivarse.
 * </p>
 */
public class LogConfigManager {

    private static final String FILE_NAME = "local.properties";
    private final Properties properties = new Properties();
    private File configFile;

    /**
     * Carga la configuración desde el archivo "local.properties" en la carpeta
     * de recursos. Si el archivo no existe, se usa una configuración vacía.
     */
    public LogConfigManager() {
        try {
            URL resource = getClass().getClassLoader().getResource(FILE_NAME);
            System.err.println(resource);
            if (resource != null) {
                configFile = new File(resource.getFile());
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    properties.load(fis);
                }
            } else {
                System.out.println("Archivo de configuración no encontrado. Se usará una configuración vacía.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la configuración: " + e.getMessage());
        }
    }

    /**
     * Habilita o deshabilita el log en archivo local. No se permite
     * deshabilitar el log de archivo si CloudWatch no está habilitado.
     *
     * @param enable {@code true} para habilitar, {@code false} para
     * deshabilitar.
     */
    public void setFileLogEnable(boolean enable) {
        boolean cloudWatchEnabled = Boolean.parseBoolean(properties.getProperty("cloudwatch.enable", "false"));
        if (!enable && !cloudWatchEnabled) {
            System.out.println("No se puede desactivar el log de archivo si CloudWatch no está habilitado.");
            return;
        }
        properties.setProperty("filelog.enable", String.valueOf(enable));
    }

    /**
     * Establece la ubicación del log en archivo local.
     *
     * @param location Ruta donde se almacenará el log.
     */
    public void setFileLogLocation(String location) {
        properties.setProperty("filelog.location", location);
    }

    /**
     * Habilita o deshabilita el log en AWS CloudWatch. Si se habilita
     *
     * @param enable {@code true} para habilitar, {@code false} para
     * deshabilitar.
     */
    public void setCloudWatchEnable(boolean enable) {
        properties.setProperty("cloudwatch.enable", String.valueOf(enable));
    }

    /**
     * Establece el nombre del grupo de logs en CloudWatch.
     *
     * @param logGroup Nombre del grupo de logs.
     */
    public void setCloudWatchLogGroup(String logGroup) {
        properties.setProperty("cloudwatch.logGroupName", logGroup);
    }

    /**
     * Establece el nombre del flujo de logs en CloudWatch.
     *
     * @param logStream Nombre del flujo de logs.
     */
    public void setCloudWatchLogStream(String logStream) {
        properties.setProperty("cloudwatch.logStreamName", logStream);
    }

    /**
     * Establece la región de AWS CloudWatch.
     *
     * @param region Región de AWS donde se almacenarán los logs.
     */
    public void setCloudWatchRegion(AwsRegion region) {
        properties.setProperty("cloudwatch.region", region.name());
    }

    /**
     * Guarda la configuración en el archivo "local.properties".
     */
    public void saveConfig() {
        if (configFile == null) {
            System.out.println("No se puede guardar la configuración. El archivo no fue encontrado.");
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(configFile)) {
            properties.store(fos, "Configuración actualizada");
            System.out.println("Configuración guardada correctamente.");
        } catch (IOException e) {
            System.out.println("Error al guardar el archivo de configuración: " + e.getMessage());
        }
    }

    /**
     * Imprime la configuración actual en la consola.
     */
    public void printConfig() {
        properties.forEach((key, value) -> System.out.println(key + " = " + value));
    }
}
