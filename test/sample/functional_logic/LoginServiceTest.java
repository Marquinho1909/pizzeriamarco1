package sample.functional_logic;

public class LoginServiceTest {
    /*
    @Mock
    UserDAO userDAO;

    OrderService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new OrderService();
        service.userDAO = userDAO;
    }

    @Test
    public void login() throws SQLException {
        UserSessionSingleton.currentSession().cleanUserSession();
        assertNull(UserSessionSingleton.currentSession().getUser());
        Mockito.when(service.userDAO.getUserByEmailAndPassword("", ""))
                .thenReturn(
                        java.util.Optional.of(new Customer(
                                1,
                                "Debby",
                                "Dummy",
                                new Customer.Address("Dummy Straße", "24", 12345),
                                'f',
                                "itzDebbyDummy@yahoo.com",
                                "12345"))
        );

        service.login("", "");
        assertEquals(1, UserSessionSingleton.currentSession().getUser().getId());
    }

    @Test
    public void isUserAdmin() {
        UserSessionSingleton.currentSession().setUser( new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"));
        assertFalse(service.isUserAdmin());
    }

    @Test
    public void isUserCustomer() {
        UserSessionSingleton.currentSession().setUser( new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345"));
        assertTrue(service.isUserCustomer());
    }
    */

}