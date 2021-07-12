package sample.functional_logic;

public class RegisterServiceTest {
    /*
    @Mock
    public UserDAO userDAO;

    RegisterService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new RegisterService();
        service.userDAO = userDAO;
    }

    @Test
    public void register() throws SQLException {
        Customer customer = new Customer(1, "Debby", "Dummy", new Customer.Address("Dummy Straße", "24", 12345), 'f', "itzDebbyDummy@yahoo.com", "12345");
        Mockito.when(service.userDAO.getUserByEmailAndPassword("itzDebbyDummy@yahoo.com", "12345")).thenReturn(java.util.Optional.of(customer));

        service.register(customer);
        Mockito.verify(service.userDAO).save(customer);
        Assert.assertEquals(customer, UserSessionSingleton.currentSession().getUser());
    }

     */
}