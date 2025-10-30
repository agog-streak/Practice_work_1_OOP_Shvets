import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

// ============== ІНТЕРФЕЙСИ ==============

// TODO 1: Створіть інтерфейс Searchable
// Цей інтерфейс повинен мати один метод:
// - boolean matches(String query) - перевіряє, чи відповідає елемент пошуковому запиту
interface Searchable {
    boolean matches(String query);
}

// TODO 2: Створіть інтерфейс Rentable
// Цей інтерфейс описує можливість орендувати елемент
// Повинен містити три методи:
// - boolean rent(String userName) - орендує елемент для користувача, повертає true якщо успішно
// - boolean returnItem() - повертає елемент з оренди, повертає true якщо успішно
// - boolean isAvailable() - повертає true, якщо елемент доступний для оренди
interface Rentable {
    boolean rent(String userName);
    boolean returnItem();
    boolean isAvailable();
}

// TODO 3: Створіть інтерфейс Displayable
// Цей інтерфейс описує можливість відображення елемента в таблиці
// Повинен містити один метод:
// - String[] toTableRow() - повертає масив рядків для відображення в таблиці
interface Displayable {
    String[] toTableRow();
}

// ============== БАЗОВІ КЛАСИ ==============

// TODO 4: Зробіть клас LibraryItem абстрактним
// Додайте слово "abstract" перед "class"
// Реалізуйте три інтерфейси: Searchable, Rentable, Displayable
// Приклад: abstract class LibraryItem implements Searchable, Rentable, Displayable
abstract class LibraryItem implements Searchable, Rentable, Displayable {
    protected String id;           // Унікальний ідентифікатор
    protected String title;        // Назва елемента
    protected int year;            // Рік видання
    protected boolean available;   // Чи доступний для оренди
    protected String rentedBy;     // Ким орендовано (null якщо вільний)
    protected LocalDate rentDate;  // Дата оренди

    public LibraryItem(String id, String title, int year) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.available = true;
        this.rentedBy = null;
        this.rentDate = null;
    }

    // TODO 5: Реалізуйте метод rent() з інтерфейсу Rentable
    // Алгоритм:
    // 1. Перевірити, чи available == true (якщо false, повернути false)
    // 2. Встановити available = false
    // 3. Зберегти userName в поле rentedBy
    // 4. Зберегти поточну дату в rentDate (використайте LocalDate.now())
    // 5. Повернути true
    @Override
    public boolean rent(String userName) {
        if (!available) return false;
        if (userName == null || userName.trim().isEmpty()) return false;
        this.available = false;
        this.rentedBy = userName.trim();
        this.rentDate = LocalDate.now();
        return true;
    }

    // TODO 6: Реалізуйте метод returnItem() з інтерфейсу Rentable
    // Алгоритм:
    // 1. Перевірити, чи available == false (якщо true, повернути false - нічого повертати)
    // 2. Встановити available = true
    // 3. Очистити rentedBy (встановити null)
    // 4. Очистити rentDate (встановити null)
    // 5. Повернути true
    @Override
    public boolean returnItem() {
        if (available) return false; // нічого повертати
        this.available = true;
        this.rentedBy = null;
        this.rentDate = null;
        return true;
    }

    // TODO 7: Реалізуйте метод isAvailable() з інтерфейсу Rentable
    // Просто поверніть значення поля available
    @Override
    public boolean isAvailable() {
        return available;
    }

    // TODO 8: Реалізуйте метод matches() з інтерфейсу Searchable
    // Алгоритм:
    // 1. Перетворити query в нижній регістр: String lowerQuery = query.toLowerCase()
    // 2. Перевірити, чи містить id (в нижньому регістрі) підрядок lowerQuery
    // 3. АБО перевірити, чи містить title (в нижньому регістрі) підрядок lowerQuery
    // 4. Повернути результат перевірки
    // Підказка: використайте метод .toLowerCase() та .contains()
    @Override
    public boolean matches(String query) {
        if (query == null) return false;
        String lowerQuery = query.toLowerCase();
        return id.toLowerCase().contains(lowerQuery) || title.toLowerCase().contains(lowerQuery);
    }

    // TODO 9: Створіть абстрактний метод getType()
    // Цей метод повинен повертати String (тип елемента: "Книга", "Журнал", "DVD")
    // Синтаксис: public abstract String getType();
    public abstract String getType();
    @Override
    public abstract String[] toTableRow();

    // Getters (не змінюйте)
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public String getRentedBy() { return rentedBy; }
}

// TODO 10: Створіть клас Book, який успадковує LibraryItem
// Додаткові поля:
// - private String author (автор книги)
// - private int pages (кількість сторінок)
//
// Що потрібно реалізувати:
// 1. Конструктор: public Book(String id, String title, int year, String author, int pages)
//    - Викличте конструктор батьківського класу: super(id, title, year)
//    - Ініціалізуйте поля author та pages
//
// 2. Метод getType() - поверніть рядок "Книга"
//
// 3. Метод toTableRow() - поверніть масив String[] з 7 елементів:
//    [0] - id
//    [1] - getType()
//    [2] - title
//    [3] - year (перетворений в String)
//    [4] - "Автор: " + author + ", Сторінок: " + pages
//    [5] - available ? "Доступна" : "Орендована"
//    [6] - rentedBy != null ? rentedBy : "-"
//
// 4. Перевизначте метод matches() - викличте super.matches(query) і додайте перевірку author
//    Приклад: return super.matches(query) || author.toLowerCase().contains(query.toLowerCase());
//
// 5. Додайте getters: getAuthor() та getPages()

class Book extends LibraryItem {
    private String author;
    private int pages;

    public Book(String id, String title, int year, String author, int pages) {
        super(id, title, year);
        this.author = author;
        this.pages = pages;
    }

    @Override
    public String getType() {
        return "Книга";
    }

    @Override
    public String[] toTableRow() {
        String[] row = new String[7];
        row[0] = id;
        row[1] = getType();
        row[2] = title;
        row[3] = String.valueOf(year);
        row[4] = "Автор: " + author + ", Сторінок: " + pages;
        row[5] = available ? "Доступна" : "Орендована";
        row[6] = rentedBy != null ? rentedBy : "-";
        return row;
    }

    @Override
    public boolean matches(String query) {
        if (super.matches(query)) return true;
        if (query == null) return false;
        String lower = query.toLowerCase();
        return author != null && author.toLowerCase().contains(lower);
    }

    public String getAuthor() { return author; }
    public int getPages() { return pages; }
}


// TODO 11: Створіть клас Magazine, який успадковує LibraryItem
// Додаткові поля:
// - private int issueNumber (номер випуску)
// - private String publisher (видавець)
//
// Що потрібно реалізувати (аналогічно Book):
// 1. Конструктор: public Magazine(String id, String title, int year, int issueNumber, String publisher)
// 2. Метод getType() - поверніть "Журнал"
// 3. Метод toTableRow() - в полі [4] поверніть "Випуск: " + issueNumber + ", Видавець: " + publisher
//    В полі [5] використайте "Доступний" : "Орендований"
// 4. Перевизначте matches() - додайте перевірку publisher
// 5. Додайте getters: getIssueNumber() та getPublisher()
class Magazine extends LibraryItem {
    private int issueNumber;
    private String publisher;

    public Magazine(String id, String title, int year, int issueNumber, String publisher) {
        super(id, title, year);
        this.issueNumber = issueNumber;
        this.publisher = publisher;
    }

    @Override
    public String getType() {
        return "Журнал";
    }

    @Override
    public String[] toTableRow() {
        String[] row = new String[7];
        row[0] = id;
        row[1] = getType();
        row[2] = title;
        row[3] = String.valueOf(year);
        row[4] = "Випуск: " + issueNumber + ", Видавець: " + publisher;
        row[5] = available ? "Доступна" : "Орендована";
        row[6] = rentedBy != null ? rentedBy : "-";
        return row;
    }

    @Override
    public boolean matches(String query) {
        if (super.matches(query)) return true;
        if (query == null) return false;
        String lower = query.toLowerCase();
        return publisher != null && publisher.toLowerCase().contains(lower);
    }

    public int getIssueNumber() { return issueNumber; }
    public String getPublisher() { return publisher; }
}



// TODO 12: Створіть клас DVD, який успадковує LibraryItem
// Додаткові поля:
// - private String director (режисер)
// - private int duration (тривалість в хвилинах)
//
// Що потрібно реалізувати (аналогічно Book):
// 1. Конструктор: public DVD(String id, String title, int year, String director, int duration)
// 2. Метод getType() - поверніть "DVD"
// 3. Метод toTableRow() - в полі [4] поверніть "Режисер: " + director + ", Тривалість: " + duration + " хв"
//    В полі [5] використайте "Доступний" : "Орендований"
// 4. Перевизначте matches() - додайте перевірку director
// 5. Додайте getters: getDirector() та getDuration()

class DVD extends LibraryItem {
    private String director;
    private int duration; // хвилини

    public DVD(String id, String title, int year, String director, int duration) {
        super(id, title, year);
        this.director = director;
        this.duration = duration;
    }

    @Override
    public String getType() {
        return "DVD";
    }

    @Override
    public String[] toTableRow() {
        String[] row = new String[7];
        row[0] = id;
        row[1] = getType();
        row[2] = title;
        row[3] = String.valueOf(year);
        row[4] = "Режисер: " + director + ", Тривалість: " + duration + " хв";
        row[5] = available ? "Доступна" : "Орендована";
        row[6] = rentedBy != null ? rentedBy : "-";
        return row;
    }

    @Override
    public boolean matches(String query) {
        if (super.matches(query)) return true;
        if (query == null) return false;
        String lower = query.toLowerCase();
        return director != null && director.toLowerCase().contains(lower);
    }

    public String getDirector() { return director; }
    public int getDuration() { return duration; }
}


// ============== GENERIC-КЛАСИ ==============

// TODO 13: Створіть generic-клас Repository<T extends LibraryItem>
// Цей клас буде зберігати колекцію елементів певного типу
//
// Поля:
// - private List<T> items - список елементів (ініціалізуйте в конструкторі як new ArrayList<>())
//
// Методи, які потрібно реалізувати:
//
// 1. public void add(T item)
//    Додає елемент до списку items
//
// 2. public boolean remove(String id)
//    Видаляє елемент за id
//    Алгоритм:
//    - Пройдіть по всіх елементах items (використайте цикл for)
//    - Якщо знайдено елемент з відповідним id, видаліть його (items.remove(i)) і поверніть true
//    - Якщо не знайдено, поверніть false
//
// 3. public T findById(String id)
//    Шукає елемент за id
//    Алгоритм:
//    - Пройдіть по всіх елементах items
//    - Якщо знайдено елемент з відповідним id, поверніть його
//    - Якщо не знайдено, поверніть null
//
// 4. public List<T> getAll()
//    Повертає копію списку всіх елементів
//    Поверніть: new ArrayList<>(items)
//
// 5. public List<T> search(String query)
//    Шукає елементи, які відповідають запиту
//    Алгоритм:
//    - Створіть новий список результатів: List<T> results = new ArrayList<>();
//    - Пройдіть по всіх елементах items
//    - Якщо item.matches(query) == true, додайте item до results
//    - Поверніть results

class Repository<T extends LibraryItem> {
    private List<T> items;

    public Repository() {
        items = new ArrayList<>();
    }

    public void add(T item) {
        if (item != null) items.add(item);
    }

    public boolean remove(String id) {
        if (id == null) return false;
        for (int i = 0; i < items.size(); i++) {
            if (id.equals(items.get(i).getId())) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    public T findById(String id) {
        if (id == null) return null;
        for (T item : items) {
            if (id.equals(item.getId())) return item;
        }
        return null;
    }

    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    public List<T> search(String query) {
        List<T> results = new ArrayList<>();
        for (T item : items) {
            if (item.matches(query)) results.add(item);
        }
        return results;
    }
}


// TODO 14: Створіть клас LibraryManager
// Цей клас управляє всіма репозиторіями
//
// Поля:
// - private Repository<Book> bookRepository
// - private Repository<Magazine> magazineRepository
// - private Repository<DVD> dvdRepository
//
// Методи, які потрібно реалізувати:
//
// 1. Конструктор LibraryManager()
//    Ініціалізуйте всі три репозиторії (створіть нові об'єкти Repository)
//
// 2. public void addItem(LibraryItem item)
//    Додає елемент до відповідного репозиторію (використовує поліморфізм)
//    Алгоритм:
//    - Якщо item instanceof Book, додайте до bookRepository (приведіть тип: (Book)item)
//    - Інакше якщо item instanceof Magazine, додайте до magazineRepository
//    - Інакше якщо item instanceof DVD, додайте до dvdRepository
//
// 3. public List<LibraryItem> getAllItems()
//    Повертає всі елементи з усіх репозиторіїв
//    Алгоритм:
//    - Створіть новий список: List<LibraryItem> allItems = new ArrayList<>();
//    - Додайте всі елементи з bookRepository.getAll() в allItems (використайте addAll)
//    - Додайте всі елементи з magazineRepository.getAll() в allItems
//    - Додайте всі елементи з dvdRepository.getAll() в allItems
//    - Поверніть allItems
//
// 4. public List<LibraryItem> searchAll(String query)
//    Шукає елементи у всіх репозиторіях
//    Алгоритм аналогічний getAllItems(), але використовуйте search(query) замість getAll()
//
// 5. public LibraryItem findById(String id)
//    Шукає елемент за id у всіх репозиторіях
//    Алгоритм:
//    - Спробуйте знайти в bookRepository.findById(id), якщо не null - поверніть
//    - Спробуйте знайти в magazineRepository.findById(id), якщо не null - поверніть
//    - Спробуйте знайти в dvdRepository.findById(id), якщо не null - поверніть
//    - Якщо нічого не знайдено, поверніть null

class LibraryManager {
    private Repository<Book> bookRepository;
    private Repository<Magazine> magazineRepository;
    private Repository<DVD> dvdRepository;

    public LibraryManager() {
        bookRepository = new Repository<>();
        magazineRepository = new Repository<>();
        dvdRepository = new Repository<>();
    }

    public void addItem(LibraryItem item) {
        if (item instanceof Book) {
            bookRepository.add((Book) item);
        } else if (item instanceof Magazine) {
            magazineRepository.add((Magazine) item);
        } else if (item instanceof DVD) {
            dvdRepository.add((DVD) item);
        }
    }

    public List<LibraryItem> getAllItems() {
        List<LibraryItem> allItems = new ArrayList<>();
        allItems.addAll(bookRepository.getAll());
        allItems.addAll(magazineRepository.getAll());
        allItems.addAll(dvdRepository.getAll());
        return allItems;
    }

    public List<LibraryItem> searchAll(String query) {
        List<LibraryItem> results = new ArrayList<>();
        results.addAll(bookRepository.search(query));
        results.addAll(magazineRepository.search(query));
        results.addAll(dvdRepository.search(query));
        return results;
    }

    public LibraryItem findById(String id) {
        LibraryItem res = bookRepository.findById(id);
        if (res != null) return res;
        res = magazineRepository.findById(id);
        if (res != null) return res;
        res = dvdRepository.findById(id);
        return res;
    }
}


// ============== GUI (НЕ ЗМІНЮЙТЕ) ==============

public class LibraryManagementApp extends JFrame {
    private LibraryManager manager;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JComboBox<String> filterCombo;

    public LibraryManagementApp() {
        manager = new LibraryManager();
        initializeTestData();
        setupUI();
    }

    private void initializeTestData() {
        // TODO 15: Додайте тестові дані
        // Створіть мінімум 3 книги, 2 журнали, 2 DVD та додайте їх через manager.addItem()
        //
        // Приклад створення книги:
        // Book book1 = new Book("B001", "Війна і мир", 1869, "Лев Толстой", 1225);
        // manager.addItem(book1);
        //
        // Приклад створення журналу:
        // Magazine mag1 = new Magazine("M001", "National Geographic", 2023, 145, "NG Society");
        // manager.addItem(mag1);
        //
        // Приклад створення DVD:
        // DVD dvd1 = new DVD("D001", "Матриця", 1999, "Вачовскі", 136);
        // manager.addItem(dvd1);

        // Ваш код тут
    }

    private void setupUI() {
        setTitle("Система управління бібліотекою");
        setSize(1100, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Верхня панель з пошуком та фільтрами
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.add(new JLabel("Пошук:"));
        searchField = new JTextField(20);
        topPanel.add(searchField);

        JButton searchBtn = new JButton("Шукати");
        topPanel.add(searchBtn);

        JButton clearSearchBtn = new JButton("Очистити");
        topPanel.add(clearSearchBtn);

        topPanel.add(new JLabel("Фільтр:"));
        filterCombo = new JComboBox<>(new String[]{"Усі", "Книги", "Журнали", "DVD", "Доступні", "Орендовані"});
        topPanel.add(filterCombo);

        add(topPanel, BorderLayout.NORTH);

        // Таблиця
        String[] columns = {"ID", "Тип", "Назва", "Рік", "Додатково", "Статус", "Орендовано"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Нижня панель з кнопками
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton rentBtn = new JButton("Орендувати");
        JButton returnBtn = new JButton("Повернути");
        JButton addBtn = new JButton("Додати елемент");
        JButton refreshBtn = new JButton("Оновити");
        JButton statsBtn = new JButton("Статистика");

        bottomPanel.add(rentBtn);
        bottomPanel.add(returnBtn);
        bottomPanel.add(addBtn);
        bottomPanel.add(refreshBtn);
        bottomPanel.add(statsBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // TODO 16: Додайте обробники подій кнопок (використовуйте лямбда-вирази)
        //
        // Для кнопки searchBtn:
        // searchBtn.addActionListener(e -> {
        //     String query = searchField.getText().trim();
        //     if (query.isEmpty()) {
        //         refreshTable();
        //     } else {
        //         List<LibraryItem> results = manager.searchAll(query);
        //         filterTable(results);
        //     }
        // });
        //
        // Для кнопки clearSearchBtn:
        // clearSearchBtn.addActionListener(e -> {
        //     searchField.setText("");
        //     filterCombo.setSelectedIndex(0);
        //     refreshTable();
        // });
        //
        // Для кнопки rentBtn:
        // rentBtn.addActionListener(e -> rentSelectedItem());
        //
        // Для кнопки returnBtn:
        // returnBtn.addActionListener(e -> returnSelectedItem());
        //
        // Для кнопки refreshBtn:
        // refreshBtn.addActionListener(e -> {
        //     searchField.setText("");
        //     filterCombo.setSelectedIndex(0);
        //     refreshTable();
        // });
        //
        // Для кнопки addBtn:
        // addBtn.addActionListener(e -> showAddDialog());
        //
        // Для кнопки statsBtn:
        // statsBtn.addActionListener(e -> showStatistics());
        //
        // Для поля searchField (щоб Enter працював як кнопка пошуку):
        // searchField.addActionListener(e -> searchBtn.doClick());
        //
        // Для filterCombo (реалізуйте фільтрацію):
        // filterCombo.addActionListener(e -> {
        //     String selected = (String) filterCombo.getSelectedItem();
        //     List<LibraryItem> filtered = manager.getAllItems();
        //     List<LibraryItem> result = new ArrayList<>();
        //
        //     if (selected.equals("Книги")) {
        //         for (LibraryItem item : filtered) {
        //             if (item instanceof Book) {
        //                 result.add(item);
        //             }
        //         }
        //     } else if (selected.equals("Журнали")) {
        //         for (LibraryItem item : filtered) {
        //             if (item instanceof Magazine) {
        //                 result.add(item);
        //             }
        //         }
        //     } else if (selected.equals("DVD")) {
        //         for (LibraryItem item : filtered) {
        //             if (item instanceof DVD) {
        //                 result.add(item);
        //             }
        //         }
        //     } else if (selected.equals("Доступні")) {
        //         for (LibraryItem item : filtered) {
        //             if (item.isAvailable()) {
        //                 result.add(item);
        //             }
        //         }
        //     } else if (selected.equals("Орендовані")) {
        //         for (LibraryItem item : filtered) {
        //             if (!item.isAvailable()) {
        //                 result.add(item);
        //             }
        //         }
        //     } else {
        //         result = filtered;
        //     }
        //     filterTable(result);
        // });

        // Ваш код тут для обробників подій

        refreshTable();
    }

    // TODO 17: Реалізуйте метод refreshTable()
    // Цей метод оновлює таблицю всіма елементами
    // Алгоритм:
    // 1. Отримайте всі елементи: List<LibraryItem> items = manager.getAllItems();
    // 2. Викличте метод filterTable(items);
    private void refreshTable() {
        // Ваш код тут
    }

    // TODO 18: Реалізуйте метод filterTable(List<LibraryItem> items)
    // Цей метод відображає список елементів у таблиці
    // Алгоритм:
    // 1. Очистіть таблицю: tableModel.setRowCount(0);
    // 2. Пройдіть по всіх елементах списку items (використайте цикл for)
    // 3. Для кожного елемента викличте item.toTableRow() і додайте рядок до таблиці:
    //    tableModel.addRow(item.toTableRow());
    private void filterTable(List<LibraryItem> items) {
        // Ваш код тут
    }

    // TODO 19: Реалізуйте метод rentSelectedItem()
    // Цей метод орендує вибраний елемент
    // Алгоритм:
    // 1. Отримайте номер вибраного рядка: int selectedRow = table.getSelectedRow();
    // 2. Якщо selectedRow == -1 (нічого не вибрано):
    //    - Покажіть повідомлення: JOptionPane.showMessageDialog(this, "Будь ласка, виберіть елемент для оренди", "Помилка", JOptionPane.WARNING_MESSAGE);
    //    - Вийдіть з методу (return)
    // 3. Отримайте ID елемента з таблиці: String id = (String) tableModel.getValueAt(selectedRow, 0);
    // 4. Знайдіть елемент: LibraryItem item = manager.findById(id);
    // 5. Якщо item == null:
    //    - Покажіть повідомлення про помилку
    //    - Вийдіть з методу
    // 6. Якщо !item.isAvailable():
    //    - Покажіть повідомлення: "Цей елемент вже орендовано користувачем: " + item.getRentedBy()
    //    - Вийдіть з методу
    // 7. Запитайте ім'я користувача:
    //    String userName = JOptionPane.showInputDialog(this, "Введіть ім'я користувача:", "Оренда елемента", JOptionPane.QUESTION_MESSAGE);
    // 8. Якщо userName != null і не порожній:
    //    - Викличте item.rent(userName.trim())
    //    - Якщо успішно, покажіть повідомлення "Елемент успішно орендовано!"
    //    - Викличте refreshTable()
    private void rentSelectedItem() {
        // Ваш код тут
    }

    // TODO 20: Реалізуйте метод returnSelectedItem()
    // Аналогічно rentSelectedItem(), але:
    // - Перевіряйте, чи елемент орендований (item.isAvailable() == false)
    // - Покажіть діалог підтвердження:
    //   int confirm = JOptionPane.showConfirmDialog(this, "Повернути елемент, орендований користувачем: " + item.getRentedBy() + "?", "Підтвердження повернення", JOptionPane.YES_NO_OPTION);
    // - Якщо confirm == JOptionPane.YES_OPTION, викличте item.returnItem()
    private void returnSelectedItem() {
        // Ваш код тут
    }

    // НЕ ЗМІНЮЙТЕ (складний GUI код)
    private void showAddDialog() {
        String[] types = {"Книга", "Журнал", "DVD"};
        String selectedType = (String) JOptionPane.showInputDialog(this,
                "Виберіть тип елемента:",
                "Додавання елемента",
                JOptionPane.QUESTION_MESSAGE,
                null,
                types,
                types[0]);

        if (selectedType == null) return;

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        JTextField idField = new JTextField();
        JTextField titleField = new JTextField();
        JTextField yearField = new JTextField();

        panel.add(new JLabel("ID:"));
        panel.add(idField);
        panel.add(new JLabel("Назва:"));
        panel.add(titleField);
        panel.add(new JLabel("Рік:"));
        panel.add(yearField);

        JTextField field1 = new JTextField();
        JTextField field2 = new JTextField();

        switch (selectedType) {
            case "Книга":
                panel.add(new JLabel("Автор:"));
                panel.add(field1);
                panel.add(new JLabel("Кількість сторінок:"));
                panel.add(field2);
                break;
            case "Журнал":
                panel.add(new JLabel("Номер випуску:"));
                panel.add(field1);
                panel.add(new JLabel("Видавець:"));
                panel.add(field2);
                break;
            case "DVD":
                panel.add(new JLabel("Режисер:"));
                panel.add(field1);
                panel.add(new JLabel("Тривалість (хв):"));
                panel.add(field2);
                break;
        }

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Введіть дані елемента",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());

                if (id.isEmpty() || title.isEmpty()) {
                    throw new IllegalArgumentException("ID та назва обов'язкові");
                }

                LibraryItem newItem = null;

                switch (selectedType) {
                    case "Книга":
                        String author = field1.getText().trim();
                        int pages = Integer.parseInt(field2.getText().trim());
                        newItem = new Book(id, title, year, author, pages);
                        break;
                    case "Журнал":
                        int issueNumber = Integer.parseInt(field1.getText().trim());
                        String publisher = field2.getText().trim();
                        newItem = new Magazine(id, title, year, issueNumber, publisher);
                        break;
                    case "DVD":
                        String director = field1.getText().trim();
                        int duration = Integer.parseInt(field2.getText().trim());
                        newItem = new DVD(id, title, year, director, duration);
                        break;
                }

                manager.addItem(newItem);
                refreshTable();
                JOptionPane.showMessageDialog(this,
                        "Елемент успішно додано!",
                        "Успіх",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Невірний формат числових даних",
                        "Помилка",
                        JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Помилка: " + ex.getMessage(),
                        "Помилка",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // НЕ ЗМІНЮЙТЕ (складний код зі статистикою)
    private void showStatistics() {
        List<LibraryItem> allItems = manager.getAllItems();

        int totalCount = allItems.size();

        // Підрахунок по типах
        int bookCount = 0, magazineCount = 0, dvdCount = 0;
        for (LibraryItem item : allItems) {
            if (item instanceof Book) bookCount++;
            else if (item instanceof Magazine) magazineCount++;
            else if (item instanceof DVD) dvdCount++;
        }

        // Підрахунок орендованих
        int rentedCount = 0;
        for (LibraryItem item : allItems) {
            if (!item.isAvailable()) rentedCount++;
        }

        double rentedPercentage = totalCount > 0 ? (rentedCount * 100.0 / totalCount) : 0;

        // Найпопулярніший рік
        Map<Integer, Integer> yearFrequency = new HashMap<>();
        for (LibraryItem item : allItems) {
            int year = item.getYear();
            yearFrequency.put(year, yearFrequency.getOrDefault(year, 0) + 1);
        }

        int mostPopularYear = 0;
        int maxCount = 0;
        for (Map.Entry<Integer, Integer> entry : yearFrequency.entrySet()) {
            if (entry.getValue() > maxCount) {
                maxCount = entry.getValue();
                mostPopularYear = entry.getKey();
            }
        }

        StringBuilder stats = new StringBuilder();
        stats.append("=== СТАТИСТИКА БІБЛІОТЕКИ ===\n\n");
        stats.append("Всього елементів: ").append(totalCount).append("\n\n");
        stats.append("За типами:\n");
        stats.append("  - Книга: ").append(bookCount).append("\n");
        stats.append("  - Журнал: ").append(magazineCount).append("\n");
        stats.append("  - DVD: ").append(dvdCount).append("\n\n");
        stats.append("Орендовано: ").append(rentedCount)
                .append(" (").append(String.format("%.1f", rentedPercentage)).append("%)\n");
        stats.append("Доступно: ").append(totalCount - rentedCount).append("\n\n");

        if (maxCount > 0) {
            stats.append("Найпопулярніший рік видання: ")
                    .append(mostPopularYear)
                    .append(" (").append(maxCount).append(" елементів)");
        }

        JTextArea textArea = new JTextArea(stats.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(this,
                scrollPane,
                "Статистика бібліотеки",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            LibraryManagementApp app = new LibraryManagementApp();
            app.setLocationRelativeTo(null);
            app.setVisible(true);
        });
    }
}