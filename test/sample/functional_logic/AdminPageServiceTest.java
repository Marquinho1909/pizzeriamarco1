package sample.functional_logic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class AdminPageServiceTest {
/*
    @Mock
    private UserDAO userDAO;
    @Mock
    private OrderDAO orderDAO;
    @Mock
    private DishDAO dishDAO;

    AdminPageService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new AdminPageService();
        service.dishDAO = dishDAO;
        service.userDAO = userDAO;
        service.orderDAO = orderDAO;

        UserSessionSingleton.currentSession().setUser(new Admin("Addi", "Dummy", 'm', "itzAddiDummy@yahoo.com", "12345"));
    }

    @Test
    public void logout() {
        service.logout();
        Assert.assertNull(UserSessionSingleton.currentSession().getUser());
    }

    @Test
    public void getCustomer() throws SQLException {
        List<Customer> dbCustomers = List.of(
                new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"),
                new Customer(2, "Max", "Mustermann", new Customer.Address("Dummy Straße", "25", 12345), 'f', "maxiBoi@web.de", "12345")
        );

        Mockito.when(service.userDAO.getAllCustomers()).thenReturn(dbCustomers);
        assertEquals(2, service.getCustomers().size());
    }

    @Test
    public void getOrders() throws SQLException {
        List<Order> dbOrders = List.of(
                new Order(1,
                        List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),2)),
                        new Date(),
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345")
                        ,
                        0.0)
        );

        Mockito.when(service.orderDAO.getAll()).thenReturn(dbOrders);
        assertEquals(1, service.getOrders().size());
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
    public void makeCustomerAdmin() throws SQLException {
        service.makeCustomerAdmin(1);

        Mockito.verify(service.userDAO).makeCustomerAdmin(1);
    }

    @Test
    public void canDishBeDeleted() throws SQLException {
        List<Order> dbOrders = List.of(
                new Order(1,
                        List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),2)),
                        new Date(),
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345")
                        ,
                        0.0)
        );
        Mockito.when(service.orderDAO.getAll()).thenReturn(dbOrders);
        assertFalse(service.canDishBeDeleted(1));
    }

    @Test
    public void deleteDish() throws SQLException {
        service.deleteDish(1);
        Mockito.verify(service.dishDAO).delete(1);
    }

    @Test
    public void deleteOrderHistory() throws SQLException {
        service.deleteOrderHistory();
        Mockito.verify(service.orderDAO).deleteAll();
    }

    @Test
    public void changeDishActivation() throws SQLException {
        service.setActivation(true, 1);
        Mockito.verify(service.dishDAO).setActivation(true, 1);
    }
*/
}