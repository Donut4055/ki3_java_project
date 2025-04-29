package ra.edu.utils;

import java.util.List;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntSupplier;

public class DataFormatter {

    /**
     * @param headers Tiêu đề các cột
     * @param rows    Danh sách bản ghi
     * @param mapper  Chuyển mỗi bản ghi T thành mảng String tương ứng với từng cột
     */
    public static <T> void printTable(String[] headers, List<T> rows, Function<T, String[]> mapper) {
        if (!rows.isEmpty()) {
            int expected = headers.length;
            int actual   = mapper.apply(rows.get(0)).length;
            if (expected != actual) {
                throw new IllegalArgumentException(
                        "Header count (" + expected +
                                ") does not match data columns (" + actual + ")"
                );
            }
        }

        int cols = headers.length;
        int[] width = new int[cols];

        // 1) Tính độ rộng ban đầu từ headers
        for (int i = 0; i < cols; i++) {
            width[i] = headers[i] != null ? headers[i].length() : 0;
        }
        // 2) Mở rộng độ rộng dựa vào dữ liệu, thay null bằng ""
        for (T row : rows) {
            String[] cells = mapper.apply(row);
            for (int i = 0; i < cols; i++) {
                String cell = (cells[i] == null ? "" : cells[i]);
                width[i] = Math.max(width[i], cell.length());
            }
        }

        // 3) Vẽ đường kẻ trên và in header
        StringBuilder sep = new StringBuilder("|");
        StringBuilder hdr = new StringBuilder("|");
        for (int i = 0; i < cols; i++) {
            sep.append("-".repeat(width[i] + 2)).append("|");
            String title = headers[i] == null ? "" : headers[i];
            hdr.append(" ").append(center(title, width[i])).append(" |");
        }
        System.out.println(sep);
        System.out.println(hdr);
        System.out.println(sep);

        // 4) In từng dòng dữ liệu
        for (T row : rows) {
            StringBuilder line = new StringBuilder("|");
            String[] cells = mapper.apply(row);
            for (int i = 0; i < cols; i++) {
                String cell = (cells[i] == null ? "" : cells[i]);
                line.append(" ").append(padRight(cell, width[i])).append(" |");
            }
            System.out.println(line);
        }
        // 5) In đường kẻ cuối
        System.out.println(sep);
    }

    /**
     *  - [C] Choose: nhập số trang
     *  - [N] Next: trang tiếp
     *  - [E] Exit: thoát
     *
     * @param headers            Tiêu đề cột
     * @param pageFetcher        Hàm lấy trang (pageIndex, pageSize) -> List<T>
     * @param totalCountSupplier Hàm trả về tổng số bản ghi (dùng để tính totalPages)
     * @param mapper             Chuyển mỗi bản ghi T thành mảng String các cột
     * @param pageSize           Số bản ghi trên mỗi trang
     */
    public static <T> void printInteractiveTable(
            String[] headers,
            BiFunction<Integer, Integer, List<T>> pageFetcher,
            IntSupplier totalCountSupplier,
            Function<T, String[]> mapper,
            int pageSize
    ) {
        Scanner scanner = new Scanner(System.in);
        int totalCount = totalCountSupplier.getAsInt();
        int totalPages = (totalCount + pageSize - 1) / pageSize;
        int page = 1;

        while (true) {
            List<T> rows = pageFetcher.apply(page, pageSize);
            printTable(headers, rows, mapper);

            System.out.printf("Page %d/%d%n", page, totalPages);
            System.out.println("[C] Choose   [N] Next   [E] Exit");
            System.out.print("Enter choice: ");
            String cmd = scanner.nextLine().trim();

            if (cmd.equalsIgnoreCase("N")) {
                if (page < totalPages) {
                    page++;
                } else {
                    System.out.println(">>> Đây là trang cuối.");
                }
            } else if (cmd.equalsIgnoreCase("C")) {
                System.out.print("Enter page number: ");
                String line = scanner.nextLine().trim();
                try {
                    int p = Integer.parseInt(line);
                    if (p >= 1 && p <= totalPages) {
                        page = p;
                    } else {
                        System.out.println(">>> Số trang không hợp lệ.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println(">>> Vui lòng nhập số nguyên hợp lệ.");
                }
            } else if (cmd.equalsIgnoreCase("E")) {
                break;
            } else {
                System.out.println(">>> Lựa chọn không hợp lệ.");
            }
        }
    }

    // Giúp căn giữa chuỗi s trong độ rộng w
    private static String center(String s, int w) {
        int pad = w - s.length();
        int left = pad / 2, right = pad - left;
        return " ".repeat(left) + s + " ".repeat(right);
    }

    // Giúp canh trái chuỗi s trong độ rộng w
    private static String padRight(String s, int w) {
        return s + " ".repeat(w - s.length());
    }
}
