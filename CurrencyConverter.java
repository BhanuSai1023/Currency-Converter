import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class CurrencyConverter {
    public static void main(String[] args) {
        // Create a Scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to choose base and target currencies
        System.out.print("Enter the base currency code (e.g., USD): ");
        String baseCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter the target currency code (e.g., EUR): ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        // Fetch real-time exchange rates
        double exchangeRate = getExchangeRate(baseCurrency, targetCurrency);

        if (exchangeRate == -1) {
            System.out.println("Failed to fetch exchange rates. Exiting.");
            return;
        }

        // Take input from the user for the amount they want to convert
        System.out.print("Enter the amount to convert from " + baseCurrency + " to " + targetCurrency + ": ");
        double amountToConvert = scanner.nextDouble();

        // Perform currency conversion
        double convertedAmount = amountToConvert * exchangeRate;

        // Display the result
        System.out.println("Converted amount: " + convertedAmount + " " + targetCurrency);

        // Close the Scanner
        scanner.close();
    }

    private static double getExchangeRate(String baseCurrency, String targetCurrency) {
        try {
            // Replace YOUR_API_KEY with a valid API key from a currency exchange rate provider
            String apiKey = "YOUR_API_KEY";
            String apiUrl = "https://api.exchangerate-api.com/v4/latest/" + baseCurrency;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse JSON response to get the exchange rate
                String jsonResponse = response.toString();
                double exchangeRate = parseExchangeRate(jsonResponse, targetCurrency);
                return exchangeRate;
            } else {
                System.out.println("Failed to fetch exchange rates. HTTP error code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static double parseExchangeRate(String jsonResponse, String targetCurrency) {
        // Parse the JSON response to get the exchange rate for the target currency
        // This example assumes a specific structure of the JSON response, and you might need to adjust it based on the API you are using
        try {
            String targetRateKey = "rates";
            int targetCurrencyIndex = jsonResponse.indexOf(targetCurrency);
            int targetRateIndex = jsonResponse.indexOf(targetRateKey, targetCurrencyIndex);
            int valueIndex = jsonResponse.indexOf(":", targetRateIndex) + 1;
            int endIndex = jsonResponse.indexOf(",", valueIndex);

            String exchangeRateString = jsonResponse.substring(valueIndex, endIndex).trim();
            return Double.parseDouble(exchangeRateString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}