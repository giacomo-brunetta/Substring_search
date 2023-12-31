import org.json.simple.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;


public class test_generator {
    static char[] alphabet = {'a','b','c','d','e','f','g','h','i','j','k','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};

    /**
     * Generate a random string of length "length" over alphabet "alphabet"
     * @param length
     * @return the generated string
     */
    public static String randomString(int length){
        char[] chars = new char[length];
        Random rnd = new Random();
        for(int i = 0; i < length; i++){
            chars[i] = alphabet[rnd.nextInt(0, alphabet.length)];
        }
        return new String(chars);
    }

    /**
     * Generate a random string of length "length" over alphabet "alphabet" in a way that makes it the
     * worst case for naif substring search
     * @param length
     * @return
     */
    public static String trickyString(int length){
        char[] chars = new char[length];
        for(int i = 0; i < length-1; i++){
            chars[i] = alphabet[1];
        }
        chars[length-1] = alphabet[2];
        return new String(chars);
    }

    /**
     * Generate a random string of length "length" over alphabet "alphabet" in a way that makes it the
     * worst case for BWT substring search
     * @param length
     * @return
     */
    public static String trickyString2(int length){
        char[] chars = new char[length];
        chars[0] = alphabet[0];
        for(int i = 1; i < length; i++){
            chars[i] = alphabet[1];
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        JSONObject json = new JSONObject();

        int len = Integer.parseInt(args[0]);
        int times = Integer.parseInt(args[1]);
        int sub_len = Integer.parseInt(args[2]);
        String output_type = args[3];

        outputType ot = outputType.fromString(output_type);

        String file_content = "";

        String str = randomString(len);
        String sub = str.substring(str.length()-sub_len);

        switch (ot){
            case JSON:
                json.put("length",len);
                json.put("times",times);

                json.put("str",str);

                json.put("sublen",sub_len);
                json.put("sub",sub);
                file_content = json.toJSONString();
                break;
            case TXT:
               file_content = times + "\n" + len + "\n" + str + "\n" + sub_len + "\n" + sub;
               break;
            case Other:
                System.out.println("Output type not supported!");
                System.exit(0);
        }
        try {
            String fileName = "test-" + len + "-" + times + "-" + sub_len + "." +  ot.extention;
            String folderName = "TEST/";
            FileWriter fileWriter = new FileWriter(folderName +  fileName);
            fileWriter.write(file_content);
            fileWriter.close();
            System.out.println("Created: "+ fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private enum outputType{
        JSON("json"),
        TXT("txt"),
        Other("");

        private String extention;

        outputType(String extention){
            this.extention = extention;
        }

        static outputType fromString(String str){
            return Arrays.stream(outputType.values())
                    .filter(x -> x.extention.equalsIgnoreCase(str))
                    .findFirst()
                    .orElse(Other);
        }
    }
}
