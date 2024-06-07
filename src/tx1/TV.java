package tx1;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class TV extends Product implements TVManager {

    static Scanner sc = new Scanner(System.in);
    public static List<TV> tvList = new ArrayList<>();

    public static final String SCREEN_SIZE = "20 inches";
    public static final String RESOLUTION = "Full HD";
    public static final boolean SMART_TV = false;

    private String screenSize;
    private String resolution;
    private boolean smartTV;

    // Constructor không tham số
    public TV() {
        super();
        this.screenSize = TV.SCREEN_SIZE;
        this.resolution = TV.RESOLUTION;
        this.smartTV = TV.SMART_TV;
    }

    // Constructor đầy đủ tham số
    public TV(String productId, String productName, double productPrice, int productTotal, String screenSize, String resolution, boolean smartTV) {
        super(productId, productName, productPrice, productTotal);
        this.screenSize = screenSize;
        this.resolution = resolution;
        this.smartTV = smartTV;
    }

    public String getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(String screenSize) {
        this.screenSize = screenSize;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public boolean isSmartTV() {
        return smartTV;
    }

    public void setSmartTV(boolean smartTV) {
        this.smartTV = smartTV;
    }

    @Override
    public String toString() {
        return String.format(" %1s | %-11s | %-10s | %-5b", super.toString(), this.screenSize, this.resolution, this.smartTV);
    }

    @Override
    public void input() {
        super.input();
        sc.nextLine();
        System.out.println("Input screen size: ");
        screenSize = sc.nextLine();
        System.out.println("Input resolution: ");
        resolution = sc.nextLine();
        System.out.println("Input smartTV: ");
        smartTV = sc.nextBoolean();
    }

    @Override
    public boolean addTV(TV t) {
        // Nhập thông tin từ người dùng
        t.input();

        // Kiểm tra xem id của sản phẩm đã tồn tại trong danh sách hay chưa
        for (TV tv : tvList) {
            if (tv.getProduct_id().equalsIgnoreCase(t.getProduct_id())) {
                // Hiển thị thông báo lỗi và trả về false nếu id đã tồn tại
                System.out.println("Error: Product ID already exists in the list.");
                return false;
            }
        }
        // Nếu id chưa tồn tại, thêm sản phẩm vào danh sách và trả về true
        return tvList.add(t);
    }

    @Override
    public boolean editTV(TV t) {
        int index = tvList.indexOf(t);
        if (index == -1) {
            System.out.println("Error: This product was not found ");
            return false;
        } else {
            System.out.println("Input new product information: ");
            t.input();
            tvList.set(index, t);
            return true;
        }
    }

    @Override
    public boolean delTV(TV t) {
        tvList.remove(t);
        return !tvList.contains(t);
    }

    @Override
    public List<TV> searchTV(String name) {
        List<TV> result = new ArrayList<>();
        for (TV tv : tvList) {
            if (tv.getProduct_name().equalsIgnoreCase(name)) {
                result.add(tv);
            }
        }
        return result;
    }


    @Override
    public List<TV> sortedTV(double price, int choice) {
        List<TV> sortedList = new ArrayList<>();
        if (choice == 1) {
            sortedList = tvList.stream()
                    .filter(tv -> tv.getProduct_price() <= price) // Lọc các TV có giá nhỏ hơn hoặc bằng price
                    .sorted(Comparator.comparingDouble(TV::getProduct_price)) // Sắp xếp theo giá
                    .toList(); // Thu thập kết quả vào một danh sách
        } else if (choice == 2) {
            sortedList = tvList.stream()
                    .filter(tv -> tv.getProduct_price() <= price) // Lọc các TV có giá nhỏ hơn hoặc bằng price
                    .sorted(Comparator.comparingDouble(TV::getProduct_price).reversed()) // Sắp xếp theo giá giảm dần
                    .collect(Collectors.toList());


        }

        return sortedList;

    }

    public static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("TV.bin"))) {
            oos.writeObject(tvList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load tvList from a file
    public static void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("TV.bin"))) {
            tvList = (List<TV>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("File 'TV.bin' not found. Creating a new file...");
            saveToFile(); // Tạo một tệp mới
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        loadFromFile();

        try {
            System.out.println("1. Add TV");
            System.out.println("2. Edit TV");
            System.out.println("3. Delete TV");
            System.out.println("4. Search TV");
            System.out.println("5. Sort TV");
            System.out.println("6. Print list");
            System.out.println("7. Exit");
            while (true) {
                System.out.print("Enter your option:");
                int option = sc.nextInt();
                switch (option) {
                    case 1: {
                        // Add a new TV
                        TV tv = new TV();
                        if (new TV().addTV(tv)) {
                            System.out.println("Added TV");
                            saveToFile();
                        }
                        break;
                    }
                    case 2: {
                        // edit a tv in tvList by entering ID
                        sc.nextLine();
                        System.out.println("Enter the product ID you want to edit: ");
                        String idToEdit = sc.nextLine();

                        for (int i = 0; i < tvList.size(); i++) {
                            TV tv = tvList.get(i);
                            if (tv.getProduct_id().equalsIgnoreCase(idToEdit)) {
                                // Hiển thị thông tin sản phẩm cũ
                                System.out.println("Current product information:");
                                System.out.println(String.format("%-10s | %-20s | %-10s | %-10s | %-10s | %-10s | %-5s", "Product ID", "Product Name", "Price", "Total", "Screen Size", "Resolution", "is SmartTV ?"));
                                System.out.println(tv);
                                new TV().editTV(tv);
                                saveToFile();
                                break;
                            }
                        }
                        break;
                    }
                    case 3: {
                        //Delete a tv by entering ID
                        sc.nextLine();
                        System.out.println("Enter the product ID you want to delete: ");
                        String idToDel = sc.nextLine();
                        for (int i = 0; i < tvList.size(); i++) {
                            TV tv = tvList.get(i);
                            if (tv.getProduct_id().equalsIgnoreCase(idToDel)) {
                                // Hiển thị thông tin sản phẩm muốn xóa
                                System.out.println("Current product information:");
                                System.out.println(String.format("%-10s | %-20s | %-10s | %-10s | %-10s | %-10s | %-5s", "Product ID", "Product Name", "Price", "Total", "Screen Size", "Resolution", "is SmartTV ?"));
                                System.out.println(tv);
                                System.out.println("Do you want to delete this product?(true/fasle)");
                                String choice = sc.nextLine();
                                if (choice.equalsIgnoreCase("true")) {
                                    new TV().delTV(tv);
                                    System.out.println("Deleted Successfully");
                                    saveToFile();
                                    break;
                                }
                            }
                        }
                        break;

                    }
                    case 4: {
                        // Search product by name
                        sc.nextLine();
                        System.out.println("Enter the product name you want to search:");
                        String name = sc.nextLine();

                        List<TV> result = new TV().searchTV(name);
                        if (result.isEmpty()) {
                            System.out.println(name + " does not exist");
                            break;
                        } else {
                            System.out.println(String.format("%-10s | %-20s | %-10s | %-10s | %-10s | %-10s | %-5s", "Product ID", "Product Name", "Price", "Total", "Screen Size", "Resolution", "is SmartTV ?"));
                            result.forEach(System.out::println);
                            break;
                        }
                    }
                    case 5: {
                        // sort products whose price is less than or equal to the price entered
                        System.out.println("Enter the price you want to sort:");
                        double price = sc.nextDouble();
                        System.out.println("1. Sort ascending");
                        System.out.println("2. Sort descending");
                        int choice = sc.nextInt();
                        List<TV> result = new TV().sortedTV(price, choice);
                        if (result.isEmpty()) {
                            System.out.println("The price is invalid");
                            break;
                        } else {
                            switch (choice) {
                                case 1: {
                                    System.out.println(String.format("%-10s | %-20s | %-10s | %-10s | %-10s | %-10s | %-5s", "Product ID", "Product Name", "Price", "Total", "Screen Size", "Resolution", "is SmartTV ?"));
                                    result.forEach(System.out::println);
                                    break;
                                }
                                case 2: {

                                    System.out.println(String.format("%-10s | %-20s | %-10s | %-10s | %-10s | %-10s | %-5s", "Product ID", "Product Name", "Price", "Total", "Screen Size", "Resolution", "is SmartTV ?"));
                                    result.forEach(System.out::println);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    case 6: {
                        // Display the tvList
                        if (tvList.isEmpty()) {
                            System.out.println("No product in the list");
                            break;
                        } else {
                            System.out.println(String.format("%-10s | %-20s | %-10s | %-10s | %-10s | %-10s | %-5s", "Product ID", "Product Name", "Price", "Total", "Screen Size", "Resolution", "is SmartTV ?"));
                            tvList.forEach(System.out::println);
                            break;
                        }
                    }
                    case 7: {
                        System.out.println("Thanks for using this application");
                        return;
                    }
                    default:
                        System.out.println("The option is invalid");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}