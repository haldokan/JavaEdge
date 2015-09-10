Feature: Retrieving an item from the provisions store should trigger uploading the item from storage if is not already
  in the store.

  Scenario: item is already in the store
    Given store has amount 123 of item cucumber
    When I get item cucumber from the store
    Then item amount 123 is returned

  Scenario: item is not in the store
    Given store has not item tomato
    When I get item tomato from the store
    Then item amount 6000 is returned