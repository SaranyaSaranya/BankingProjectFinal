Feature: Banking End-to-End Flow
  Supports dynamic data with explicit waits and step-based setup/teardown

  Scenario: Setup
    Given The user logins the page

  Scenario Outline: Manager adding the customer and opens the account
    Given The bank manager navigates to home dashboard
    When The manager adds customer "<firstName>" "<lastName>" with post code "<postCode>"
    And The add customer alert appears and is accepted
    And The manager opens account for "<firstName>" "<lastName>" with currency index <currencyIndex>
    Then The account creation alert appears and is accepted

    Examples:
      | firstName | lastName | postCode | currencyIndex |
      | Customer1 | Dummy    | 12345    | 1             |

  Scenario Outline: Customer deposits and withdraws then verifies the transactions
    Given The customer logging as "<fullName>"
    And The customer selects account index <accountIndex>
    When The customer deposits <depositAmount>
    And The customer withdraws <withdrawAmount>
    Then The transactions show latest type "<latestType>" amount <latestAmount>
    And The balance resets to <resetBalance>

    Examples:
      | fullName        | accountIndex | depositAmount | withdrawAmount | latestType | latestAmount | resetBalance |
      | Customer1 Dummy | 0            | 500           | 100            | Debit      | 100          | 0            |

  Scenario: Teardown
    Then The user closes the browsers
