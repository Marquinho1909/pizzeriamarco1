package sample.functional_logic;

public class DishCreationModalServiceTest {
/*
    @Mock
    CategoryDAO categoryDAO;
    @Mock
    DishDAO dishDAO;

    CouponService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new CouponService();
        service.categoryDAO = categoryDAO;
        service.dishDAO = dishDAO;
    }

    @Test
    public void deleteCategories() throws SQLException {
        service.deleteCategories(List.of(new Category(1, "Pizza")));
        Mockito.verify(service.categoryDAO).deleteAll(List.of(new Category(1, "Pizza")));
    }

    @Test
    public void saveDish() throws SQLException {
        service.saveDish(new Dish("Lasagne", List.of(), 0.1 , true));
        Mockito.verify(service.dishDAO).save(new Dish("Lasagne", List.of(), 0.1 , true));
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
    public void doesCategoryExist() throws SQLException {
        List<Category> dbCategories = List.of(
                new Category(1, "Pizza"),
                new Category(2, "Nudeln")
        );
        Mockito.when(service.categoryDAO.getAll()).thenReturn(dbCategories);

        assertTrue(service.doesCategoryExist("Pizza"));
        assertFalse(service.doesCategoryExist("Lasagne"));
    }

    @Test
    public void saveCategory() throws SQLException {
        service.saveCategory(new Category("Salat"));

        Mockito.verify(service.categoryDAO).save(new Category("Salat"));
    }

    @Test
    public void canAllCategoriesBeDeleted() throws SQLException {
        List<Dish> dbDishes = List.of(
                new Dish(1, "Spaghetti", List.of(new Category(1, "Nudeln")), 2.0,true),
                new Dish(2, "Makkaroni", List.of(new Category(1, "Nudeln")), 3.0,true)
        );

        Mockito.when(service.dishDAO.getAll()).thenReturn(dbDishes);
        assertFalse(service.canAllCategoriesBeDeleted(List.of(
                new Category(1, "Pizza"),
                new Category(2, "Nudeln")
        )));
        assertTrue(service.canAllCategoriesBeDeleted(List.of(
                new Category(1, "Pizza"),
                new Category(2, "Lasagne")
        )));
    }
*/
}