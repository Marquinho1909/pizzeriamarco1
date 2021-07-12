package sample.functional_logic;

public class OrderModalServiceTest {
    /*
    @Mock
    public OrderDAO orderDAO;
    @Mock
    public UserDAO userDAO;
    @Mock
    public CouponDAO couponDAO;

    OrderModalService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new OrderModalService();
        service.orderDAO = orderDAO;
        service.couponDAO = couponDAO;
        service.userDAO = userDAO;

        UserSessionSingleton.currentSession().setUser(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"));
    }

    @Test
    public void order() throws SQLException {
        service.ordersInCart.add(new OrderPosition(1,
                new Dish(1, "Spaghetti",
                        List.of(new Category(1, "Nudeln")),
                        2.0,
                        true),1));

        Date date = new Date();

        Mockito.when(service.orderDAO.save(
                new Order(
                        List.of(new OrderPosition(1,
                                new Dish(1, "Spaghetti",
                                        List.of(new Category(1, "Nudeln")),
                                        2.0,
                                        true),1)),
                        date,
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"),
                        0)
        )).thenReturn(1);

        service.order(false, date);

        Mockito.verify(service.orderDAO).save(
                new Order(
                        List.of(new OrderPosition(1,
                                new Dish(1, "Spaghetti",
                                        List.of(new Category(1, "Nudeln")),
                                        2.0,
                                        true),1)),
                        date,
                        new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"),
                        0)
        );
    }

    @Test
    public void fetchCoupon() throws SQLException {
        service.coupon = null;
        Mockito.when(service.userDAO.getCustomerCoupon(new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345")))
                .thenReturn(java.util.Optional.of(new Coupon(1, 0.2)));
        service.fetchCoupon();
        Assert.assertEquals(1, service.coupon.getId());
    }

     */
}