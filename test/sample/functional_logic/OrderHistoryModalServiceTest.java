package sample.functional_logic;

public class OrderHistoryModalServiceTest {
/*
    @Mock
    OrderDAO orderDAO;

    DishService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new DishService();
        service.orderDAO = orderDAO;
        UserSessionSingleton.currentSession().setUser( new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"));
    }

    @Test
    public void getOrdersFromLoggedInUser() throws SQLException {
        List<Order> dbOrders = List.of(
                new Order(1,
                        List.of(new OrderPosition(1, new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),2)),
                        new Date(),
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345")
                        ,
                        0.0)
        );

        Mockito.when(service.orderDAO.getAllByUserId(1)).thenReturn(dbOrders);

        assertEquals(1, service.getOrdersFromLoggedInUser().size());
    }
*/
}