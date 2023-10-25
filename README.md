# Dungeon Crawl (sprint 2)

## Story

Last week you created a pretty good [Roguelike](https://en.wikipedia.org/wiki/Roguelike) game.
It already has some features, but the players have no opportunity to save their games.
It can be annoying, especially when you have to leave the game suddenly.

The gamer community is begging you for saving functionality and some other new and interesting ideas, such as:

- game sharing between players
- maps of different sizes
- player-tracking camera movement

Management is handing out a **prioritized list** of new user stories that must be
appended to the unfinished stories from last week in your product backlog.
Try to estimate these new stories as well, and, based on the estimations,
pick the stories your team can finish in this sprint.

> Using database for saving game state feature is a business critical item which overrides every other priority now!

Continue this entertaining project and make our players happier!

## What are you going to learn?

- Serialize objects.
- Communicate with databases.
- Write unit tests for your classes.
- Understand the **Data Access Object** design pattern.

## Tasks

1. Create a new sprint from the existing backlog. Last week, you had a long list of stories, and there are a few new stories this week.
    - The new items are added to the backlog.
    - The team has created a new sprint plan, based on the unified backlog.
    - The mandatory "Saving game" backlog item is in Sprint 2 and planned in detail.

2. As you work in a new repository, but need the code from the previous sprint, add the `dungeon-crawl-2` repository as a new remote to the repository of the previous sprint, then pull (merge) and push your changes into it.
    - There is a merge commit in the project repository that contains code from the previous sprint.

3. Allow the user to save the current state of the game in a database. Extend the given schema if needed.
    - The application uses a PostgreSQL database with the schema in `schema_ddl.sql`.
    - The application respects the `PSQL_USER_NAME`, `PSQL_PASSWORD`, and `PSQL_DB_NAME` environment variables.
    - An Entity Relationship diagram (connections between classes, 1-1, 1-many, and so on) is created in a digitalized format.
    - When the user presses `Ctrl+S`, a modal window pops up with a text input field (labelled `Name`) and two buttons, `Save` and `Cancel`.
- When the user clicks `Save`, the game saves the current state (current map, player position, and inventory content) in the database.
  - If the given name is new, the state is saved.
  - If the given username already exist in the db, the system shows a dialogbox with the `Would you like to overwrite the already existing state?` question.
    - When choosing `Yes`, the already existing state is updated and all modal windows close.
    - When choosing `No`, the dialog closes and the name input field content on the saving dialog is selected again.
  - When choosing `Cancel`, the saving process terminates without any further action.
- The modal window is automatically closed after the operation.
    - Already discovered maps are also saved in the DB.
    - There is a `Load` menu, which brings up a modal window, showing the previously saved states with their names as a selectable list. Choosing an element loads the selected game state with the proper map, position, and inventory.

4. Allow the user to export (serialize) their game state into a text file, and load (deserialize) the game from the exported file.
    - There is a menu item with an `Export game` label, which triggers the export mechanism.
    - The exporting process asks the user for the location and the name of the exported file. The file is created in the defined directory, using the given name as a JSON file. For example, `<my-fantastic-game>.json`.
    - The file stores every necessary game state information in a JSON format.
    - There is a menu button labeled `Import` for importing a previously saved game.
- The system shows a file browser to select an exported file.
  - If the chosen file is not in the proper format, the application shows a dialog window (OK, Cancel) with the `IMPORT ERROR! Unfortunately the given file is in wrong format. Please try another one!` message.
    - If the user clicks `OK`, the window closes without any further action.
    - If the user click `Cancel`, all dialogs and modal windows close.
  - If the file is in the required format, the game loads the state, and navigates to the point on the map where the user left the game with the inventory.

5. The customer looks for quality assurance and wants to see that your code is covered by unit tests. It is important to also cover negative scenarios, not only positive test cases.
    - Every unit test method is well-arranged and follows the `arrange`-`act`-`assert` structure.
    - Unit test classes and methods adhere to the following naming conventions consistently.
- classes: `<The name of the tested class>Test`
- methods: `<the name of the tested method>_<expected input / tested state>_<expected behavior>`
    - Every test class has at least one negative test case (or more, if it is plausible).
    - Code coverage of self-created business logic classes is above 90%.

## General requirements

None

## Hints

- Break down the backlog items into smaller tasks so that you can work in parallel.
- The given DB schema is only an example. You probably need to alter it,
  according to the requirements. For example, it doesn't contain any information
  on the inventory, or on maps discovered by the player.
- Write as many unit tests as possible to cover your business logic.
- Set up a test for getting `null` as an argument for methods that take a reference type parameter. These are called negative test cases.
- Read the value of an environment variable using `System.getenv("VAR_NAME");`.
- Import the sample data file into `psql` with the `\i` command or run it using the Database tool in IntelliJ.
- In IntelliJ, language injections allow you to work with pieces of code in other
  languages embedded in your code. When you inject a language (such as PostgreSQL)
  into a string literal, you get comprehensive code assistance for editing that literal.
- [Set](https://www.jetbrains.com/help/objc/add-environment-variables-and-program-arguments.html) environment variables for your run configuration.
- Add a necessary dependency to your `pom.xml` and reload the Maven project for serialization.


## Background materials

- <i class="far fa-exclamation"></i> [Software testing](project/curriculum/materials/pages/general/software-testing.md)
- <i class="far fa-book-open"></i> [Positive or negative](https://stackoverflow.com/questions/8162423)
- <i class="far fa-exclamation"></i> [How to design classes](project/curriculum/materials/pages/java/how-to-design-classes.md)
- <i class="far fa-exclamation"></i> [Introduction to JDBC](project/curriculum/materials/competencies/java-intermediate/java-db-access.md.html)
- <i class="far fa-exclamation"></i> [JDBC basics](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
- <i class="far fa-exclamation"></i> [DAO pattern in Java](https://www.baeldung.com/java-dao-pattern)
- <i class="far fa-exclamation"></i> [Serialization in Java](project/curriculum/materials/pages/java/serialization-in-java.md)
- <i class="far fa-exclamation"></i> [Compare two popular serialization frameworks](https://www.baeldung.com/jackson-vs-gson)
- [1-Bit Pack by Kenney](https://kenney.nl/assets/bit-pack)
