package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BuildDB {
    public static void main(String[] args) {
        //roomFeature();
        reservationFeature();
    }

    private static void roomFeature() {
        String[] names = {"James", "Marguerite", "Drake", "Grey", "Gillian", "Rayleen", "Javan", "Kathryn", "Bree", "Noel", "June", "Love", "Warren", "Nadeen", "Blaise", "Eli", "Lucinda", "Ellison", "Porter", "Bailee"};
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\talev\\OneDrive\\Desktop\\Study\\room_feature.sql");
            for (int i = 37; i <= 98; i++) { // room id
                int ftrsAmt = (int) ((Math.random() * (7 - 1)) + 1);
                List<Integer> ftrIds = new ArrayList<>(6);
                for (int j = 0; j < ftrsAmt; j++) { // features
                    int ftrid = (int) ((Math.random() * (7 - 1)) + 1);
                    if (ftrIds.contains(ftrid)) {
                        j--;
                    } else {
                        ftrIds.add(ftrid);
                    }
                }
                for (int j = 0; j < ftrsAmt; j++) { // features
                    myWriter.write("INSERT INTO `hoteldb`.`room_feature` (`room_id`, `feature_id`) VALUES ");
                    myWriter.write("('" + i + "', '" + ftrIds.get(j) + "');\n");
                }
            }
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Successfully wrote to the file.");
    }

    private static void reservationFeature() {
        String[] names = {"James", "Marguerite", "Drake", "Grey", "Gillian", "Rayleen", "Javan", "Kathryn", "Bree", "Noel", "June", "Love", "Warren", "Nadeen", "Blaise", "Eli", "Lucinda", "Ellison", "Porter", "Bailee"};
        try {
            FileWriter myWriter = new FileWriter("C:\\Users\\talev\\OneDrive\\Desktop\\Study\\res_feature.sql");
            for (int i = 45; i <= 84; i++) { // room id
                for (int j = 1; j <= 6; j++) { // features
                    myWriter.write("INSERT INTO `hoteldb`.`reservation_feature` " +
                            "(`reservation_number`, `feature_id`, `importance`) VALUES " +
                            "('" + i + "', '" + j + "', '" + (int) ((Math.random() * (4 - 1)) + 1) + "');\n");
                }
            }
            myWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Successfully wrote to the file.");
    }
}
