import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class CategoryTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Category.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfNamesAretheSame() {
    Category firstCategory = new Category("Household chores");
    Category secondCategory = new Category("Household chores");
    assertTrue(firstCategory.equals(secondCategory));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    assertTrue(Category.all().get(0).equals(myCategory));
  }

  @Test
  public void find_findCategoryInDatabase_true() {
    Category myCategory = new Category("Household chores");
    myCategory.save();
    Category savedCategory = Category.find(myCategory.getId());
    assertTrue(myCategory.equals(savedCategory));
  }

  @Test
  public void addTask_addsTaskToCategory() {
    Category myCategory = new Category("Household chorse");
    myCategory.save();

    Task myTask = new Task("Mow the lawn");
    myTask.save();

    myCategory.addTask(myTask);
    Task savedTask = myCategory.getTasks().get(0);
    assertTrue(myTask.equals(savedTask));
  }

  @Test
  public void getTasks_returnsAllTasks_ArrayList() {
    Category myCategory = new Category("Household chores");
    myCategory.save();

    Task myTask = new Task("Mow the lawn");
    myTask.save();

    myCategory.addTask(myTask);
    List savedTasks = myCategory.getTasks();
    assertEquals(savedTasks.size(), 1);
  }

  @Test
  public void addCategory_addsCategorytoTask(){
    Category myCategory = new Category("Household chores");
    myCategory.save();

    Task myTask = new Task("Mow the lawn");
    myTask.save();

    myTask.addCategory(myCategory);
    Category savedCategory = myTask.getCategories().get(0);
    assertTrue(myCategory.equals(savedCategory));
  }

  @Test
  public void getCategories_returnsAllCategories_ArrayList() {
    Category myCategory = new Category("Mortuary Chores");
    myCategory.save();

    Task myTask = new Task("Dust");
    myTask.save();

    myTask.addCategory(myCategory);
    List savedCategories = myTask.getCategories();
    assertEquals(savedCategories.size(), 1);
  }
	// @Test
	// public void getTasks_retrievesAllTasksFromDatabase_tasksList() {
	// 	Category myCategory = new Category("Household chores");
	// 	myCategory.save();
	// 	Task firstTask = new Task("Mow the lawn", myCategory.getId(), "2016-02-24T03:58");
	// 	firstTask.save();
	// 	Task secondTask = new Task("Do the dishes", myCategory.getId(), "2016-02-24T03:58");
	// 	secondTask.save();
	// 	Task[] tasks = new Task[] {firstTask, secondTask};
	// 	assertTrue(myCategory.getTasks().containsAll(Arrays.asList(tasks)));
	// }
}
