package database;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Created by dinht_000 on 10/22/2015.
 */
public class User {
    private String name;
    private String productKey = null;
    //private String machineMACAdd = null;
    private boolean validUser = false;

    public User() {
        checkLicenseFromMachine();
    }

    private void checkLicenseFromMachine() {
        File f = new File("./data/license.log");
        if (!f.exists()) {
            return ;
        }

        try {
            Scanner in = new Scanner(f);
            if (in.hasNextLine()) {
                name = in.nextLine();
            }
            if (in.hasNextLine()) {
                productKey = in.nextLine();
                if (productKey.equals(generateKey())) {
                    setValidUser(true);
                }
            }

            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeLisence() {
        try {
            File f = new File("./data/license.log");
            if (!f.exists()) {
                f.createNewFile();
            }

            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(f)));

            System.out.println("Writing license to hard disk: " + name + ": " + productKey);
            writer.write(String.format("%s\n%s", name, productKey));
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean isValidUser() {
        return validUser;
    }

    public void setValidUser(boolean validUser) {
        this.validUser = validUser;
    }

    public boolean auth(String inputKey){
        return (inputKey.equals(productKey)) ? true : false;
    }

    public void setName(String n){
        name = n;
    }

    public String getName(){ return name;}

    public String generateKey(){
        MessageDigest digester = null;
        try{
            digester = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        int pos = 1, keyCount = 0;

        if (name == null || name.length() == 0) return null;

        digester.update(name.getBytes());
        byte[] hash = digester.digest();
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < hash.length; i++) {
            if ((0xff & hash[i]) < 0x10) {
                hexString.append("0" + Integer.toHexString((0xFF & hash[i])));
            }
            else {
                hexString.append(Integer.toHexString(0xFF & hash[i]));
            }
        }

        productKey = hexString.toString().toUpperCase();
        while(pos < productKey.length()){
            if((pos % 4 == 0) && (pos + keyCount) < productKey.length()){
                productKey = productKey.substring(0, pos + keyCount) + "-" + productKey.substring(pos + keyCount, productKey.length());
                keyCount++;
                pos++;
            }
            pos++;
        }
        return productKey;
    }

    public String getProductKey(){
        return productKey;
    }

//    public String getMachineMACAdd() {
//        return machineMACAdd;
//    }
//
//    public void setMachineMACAdd(String machineMACAdd) {
//        this.machineMACAdd = machineMACAdd;
//    }
}
