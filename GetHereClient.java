import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GetHereClient {
    private static final int CIPHER_KEY_SEND = 17;
    private static final int CIPHER_KEY_RECEIVE = 13;
    private static final Map<String, Integer> ALPHABET = new HashMap<>();

    public static void main(String[] args) throws Exception {
        addAllAlphabetCharacter();
        System.out.println("Client start... >");
        InetAddress IPAddress = InetAddress.getByName("localhost");
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            while (true) {
                BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
                String sentence = inFromUser.readLine();
                if (sentence == null || sentence.isEmpty()) {
                    System.out.println("Invalid message, try again...");
                    continue;
                }
                byte[] sendData = new byte[sentence.length()];
                String encryptData = encrypt(sentence, CIPHER_KEY_SEND);
                sendData = encryptData.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
                System.out.printf("Sending: %s\nEncrypt: %s%n", sentence, encryptData);
                clientSocket.send(sendPacket);
                byte[] receiveData = new byte[sentence.length() * 3 + 2 * 3];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String receivedData = new String(receivePacket.getData());
                String decryptData = decrypt(receivedData, CIPHER_KEY_RECEIVE);
                System.out.printf("RECEIVED FROM SERVER: %s\nDecrypt: %s%n", receivedData, decryptData);
            }
        }
    }

    private static String encrypt(String rawText, int cipherKey) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rawText.length(); i++) {
            char letter = rawText.charAt(i);
            Integer index = ALPHABET.get(String.valueOf(letter).toLowerCase());

            if (index != null) {
                int newIndex = index + cipherKey;
                int encryptIndex = newIndex >= ALPHABET.size() ? newIndex - ALPHABET.size() : newIndex;
                String encryptLetter = letterFrom(encryptIndex);
                builder.append(Character.isUpperCase(letter) ? encryptLetter.toUpperCase() : encryptLetter);
            } else {
                builder.append(rawText.charAt(i));
            }

        }
        return builder.toString();
    }

    private static String decrypt(String encryptedValue, int cipherKey) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < encryptedValue.length(); i++) {
            char letter = encryptedValue.charAt(i);
            Integer index = ALPHABET.get(String.valueOf(letter).toLowerCase());

            if (index != null) {
                int newIndex = index - cipherKey;
                int encryptIndex = newIndex < 0 ? ALPHABET.size() - (cipherKey - index) : newIndex;
                String encryptLetter = letterFrom(encryptIndex);
                builder.append(Character.isUpperCase(letter) ? encryptLetter.toUpperCase() : encryptLetter);
            } else {
                builder.append(encryptedValue.charAt(i));
            }
        }

        return builder.toString();
    }

    private static String letterFrom(int encryptIndex) {
        return ALPHABET.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), encryptIndex))
                .map(Map.Entry::getKey)
                .findFirst()
                .get();
    }

    private static void addAllAlphabetCharacter() {
        ALPHABET.put("a", 0);
        ALPHABET.put("b", 1);
        ALPHABET.put("c", 2);
        ALPHABET.put("d", 3);
        ALPHABET.put("e", 4);
        ALPHABET.put("f", 5);
        ALPHABET.put("g", 6);
        ALPHABET.put("h", 7);
        ALPHABET.put("i", 8);
        ALPHABET.put("j", 9);
        ALPHABET.put("k", 10);
        ALPHABET.put("l", 11);
        ALPHABET.put("m", 12);
        ALPHABET.put("n", 13);
        ALPHABET.put("o", 14);
        ALPHABET.put("p", 15);
        ALPHABET.put("q", 16);
        ALPHABET.put("r", 17);
        ALPHABET.put("s", 18);
        ALPHABET.put("t", 19);
        ALPHABET.put("u", 20);
        ALPHABET.put("v", 21);
        ALPHABET.put("w", 22);
        ALPHABET.put("x", 23);
        ALPHABET.put("y", 24);
        ALPHABET.put("z", 25);
    }
}
