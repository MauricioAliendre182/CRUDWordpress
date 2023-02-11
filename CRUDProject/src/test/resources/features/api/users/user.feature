@APIUsers @API
Feature: API Posts

  @GetAllUsers
  Scenario Outline: A user with a proper role should be able to retrieve all users
    Given I am authenticated with "<User Role>" role
    When I make a request to retrieve all users
    Then response should be "<Status Line>"
     And response should be valid and have a body
     And response should have a proper amount of users

  Examples:
    | User Role     | Status Line     |
    | administrator | HTTP/1.1 200 OK |

  @CreateAUser
  Scenario Outline: A user with proper role should be able to create a user
    Given I am authenticated with "<User Role>" role
    When I make a request to create a user with the following params
      | username    | email                 | password |
      | Jhonatan123 | Jhonny123@hotmail.com | 123456   |
    Then response should be "<Status Line>"
     And response should be valid and have a body
     And user should have been created with the proper params

  Examples:
    | User Role     | Status Line          |
    | administrator | HTTP/1.1 201 Created |

  @RetrieveAUser
  Scenario Outline: A user with proper role should be able to retrieve a user
    Given I am authenticated with "<User Role>" role
    When I make a request to retrieve a user
    Then response should be "<Status Line>"
     And response should be valid and have a body
     And user should have been retrieved with the proper params

  Examples:
    | User Role     | Status Line     |
    | administrator | HTTP/1.1 200 OK |

  @UpdateAUser
  Scenario Outline: A user with proper role should be able to update a user
    Given I am authenticated with "<User Role>" role
    When I make a request to update a user with the following params
      | nickname | email                 | password  |
      | Clark123 | Clark1998@gmail.com   | 1234567890|
    Then response should be "<Status Line>"
     And response should be valid and have a body
     And user should have been updated with the proper params

  Examples:
    | User Role     | Status Line     |
    | administrator | HTTP/1.1 200 OK |

  @DeleteAUser
  Scenario Outline: A user with proper role should be able to delete a user
    Given I am authenticated with "<User Role>" role
    When I make a request to delete a user
    Then response should be "<Status Line>"
     And response should be valid and have a body
     And user should have been deleted

  Examples:
    | User Role     | Status Line     |
    | administrator | HTTP/1.1 200 OK |