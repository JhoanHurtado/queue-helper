/**
 * Modelo de conexión a AWS que carga configuraciones desde un archivo de propiedades local.
 * <p>
 * Esta clase se encarga de leer las configuraciones necesarias para la conexión a AWS CloudWatch
 * desde un archivo de propiedades denominado "local.properties".
 * </p>
 * 
 * @author Jhoan Hurtado
 */
package io.github.jhoanhurtado.domain.models;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import io.github.jhoanhurtado.enums.AwsRegion;

/**
 * Clase que representa el modelo de conexión a AWS.
 */
public class AwsConnectionModel {

    /** Nombre del archivo de configuración. */
    private static final String FILE_NAME = "local.properties";
    
    /** Región de AWS configurada. */
    private AwsRegion awsRegion;
    
    /** Nombre del grupo de logs en AWS CloudWatch. */
    private String logGroupName;
    
    /** Nombre del flujo de logs en AWS CloudWatch. */
    private String logStreamName;

    /**
     * Constructor de la clase que inicializa la configuración desde el archivo de propiedades.
     */
    public AwsConnectionModel() {
        loadConfiguration();
    }

    /**
     * Carga la configuración desde el archivo de propiedades.
     * <p>
     * Este método intenta leer el archivo "local.properties" y extrae los valores de configuración
     * para la región de AWS, el grupo de logs y el flujo de logs. Si no se encuentra el archivo,
     * se informa en la salida estándar.
     * </p>
     */
    private void loadConfiguration() {
        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(FILE_NAME)) {
            if (inputStream != null) {
                properties.load(inputStream);
                this.awsRegion = AwsRegion.valueOf(properties.getProperty("cloudwatch.region", "US_EAST_1"));
                this.logGroupName = properties.getProperty("cloudwatch.logGroupName", "");
                this.logStreamName = properties.getProperty("cloudwatch.logStreamName", "");
            } else {
                System.out.println("El archivo de configuración no fue encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Error al cargar la configuración: " + e.getMessage());
        }
    }

    /**
     * Obtiene la región de AWS configurada.
     * 
     * @return La región de AWS.
     */
    public AwsRegion getAwsRegion() {
        return awsRegion;
    }

    /**
     * Obtiene el nombre del grupo de logs en AWS CloudWatch.
     * 
     * @return El nombre del grupo de logs.
     */
    public String getLogGroupName() {
        return logGroupName;
    }

    /**
     * Obtiene el nombre del flujo de logs en AWS CloudWatch.
     * 
     * @return El nombre del flujo de logs.
     */
    public String getLogStreamName() {
        return logStreamName;
    }
}