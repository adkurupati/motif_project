import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Reads configuration file - typically config.txt
 * Stores config values in key/value pairs
 */

public class ConfigReader {
    private Map<String, String> configMap;

    public ConfigReader(String filePath) {
        this.configMap = new HashMap<>();
        readConfigFile(filePath);
    }

    private void readConfigFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip comments and empty lines
                if (line.trim().isEmpty() || line.startsWith("#")) {
                    continue;
                }

                // Split the line into key and value
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    configMap.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getValue(String key) {
        return configMap.get(key);
    }

    // Convert string param to int value
    public int getIntValue(String key) {
        int intValue = 0;
        String strValue = configMap.get(key);
        try {
            intValue = Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Unable to convert");
            System.out.println(strValue);
        }
        return intValue;
    }

    // Convert string param to double value
    public double getDoubleValue(String key) {
        double doubleValue = 0;
        String strValue = configMap.get(key);
        try {
            doubleValue = Double.parseDouble(strValue);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Unable to convert");
            System.out.println(strValue);
        }
        return doubleValue;
    }

}

