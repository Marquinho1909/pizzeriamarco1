package sample.functional_logic;

public class CustomerPageServiceTest {
/*    @Mock
    DishDAO dishDAO;
    @Mock
    CategoryDAO categoryDAO;

    CategoryService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new CategoryService();
        service.dishDAO = dishDAO;
        service.categoryDAO = categoryDAO;

        UserSessionSingleton.currentSession().setUser( new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"));
    }

    @Test
    public void logout() {
        service.logout();
        Assert.assertNull(UserSessionSingleton.currentSession().getUser());
    }

    @Test
    public void getCategories() throws SQLException {
        List<Category> dbCategories = List.of(
            new Category(1, "Pizza"),
            new Category(2, "Nudeln")
        );
        Mockito.when(service.categoryDAO.getAll()).thenReturn(dbCategories);

        assertEquals(2, service.getCategories().size());
    }

    @Test
    public void getDishes() throws SQLException {
        List<Dish> dbDishes = List.of(
                new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),
                new Dish(2, "Makkaroni", List.of(new Category(1, "Nudeln")), 3.0,true)
        );

        Mockito.when(service.dishDAO.getAll()).thenReturn(dbDishes);

        assertEquals(2, service.getDishes().size());
    }

    @Test
    public void addedToExistingDish() {
        service.ordersInCart = new ArrayList<>();
        service.ordersInCart.add(new OrderPosition(1,
                new Dish(1, "Spaghetti",
                        List.of(new Category(1, "Nudeln")),
                        2.0,
                        true),1));

        assertTrue(service.addedToExistingDish(new Dish(1, "Spaghetti",
                List.of(new Category(1, "Nudeln")),
                2.0,
                true),1));
        assertEquals(1, service.ordersInCart.size());
        assertEquals(2, service.ordersInCart.get(0).getAmount());
    }

    @Test
    public void addOrderToCart() {
        service.ordersInCart = new ArrayList<>();

        service.addOrderToCart(
                new OrderPosition(
                        1,
                        new Dish(1,
                                "Spaghetti",
                                List.of(new Category(1, "Nudeln")),
                                2.0,
                                true),
                        2));

        assertEquals(1, service.ordersInCart.size());
    }

    @Test
    public void removeOrder() {
        service.ordersInCart = new ArrayList<>();
        service.ordersInCart.add(new OrderPosition(1,
                new Dish(1, "Spaghetti",
                        List.of(new Category(1, "Nudeln")),
                        2.0,
                        true),2));

        service.removeOrder(1);

        assertTrue(service.ordersInCart.isEmpty());
    }
*/
}