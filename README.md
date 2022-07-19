# InventoryUtil

The goal of this project is to provide a complete solution to item manipulation for Spigot plugins.

The methods provided in the Inventory interface lack the flexibility required for many kinds of item manipulation operations required by plugin developers. This library provides a set of item manipulation methods which should satisfy a far broader range of item manipulation needs.

## Examples

```java
ItemGiver.giveItems(player, SlotGroups.PLAYER_MAIN_INV, items);
ItemGiver.giveItems(container, SlotGroups.ENTIRE_INV, items);
// Given items will be put into empty slots. Partial stacks will be ignored
ItemGiver.giveItems(inventory, SlotGroups.ENTIRE_INV.filterSlots(ItemPredicates.onlyEmptyItems()), items);

ItemGiver.setItems(player, SlotGroups.PLAYER_ENTIRE_INV, savedInventory);
ItemGiver.setItems(player, SlotGroups.PLAYER_HOTBAR, lobbyItems);
```

```java
ItemRemover.clearSlots(player, SlotGroups.PLAYER_ENTIRE_INV);
ItemRemover.clearSlots(player, SlotGroups.PLAYER_MAIN_INV.filterSlots(ItemPredicates.checkNamePredicate((itemName) -> itemName.startsWith("Orange"), true)));
ItemRemover.clearItems(inventory, SlotGroups.ENTIRE_INV, itemToClear);
ItemRemover.removeItems(player, SlotGroups.PLAYER_ENTIRE_INV, items);
ItemRemover.removeItem(player, SlotGroups.PLAYER_MAIN_INV, ItemPredicates.requireSimilarity(item), amountToRemove);
```

```java
if (ItemUtil.hasRoomForItems(player, SlotGroups.PLAYER_MAIN_INV, itemsToGive)) {}
if (ItemUtil.hasAllItems(player, SlotGroups.PLAYER_MAIN_INV, items)) {}
if (ItemUtil.isEmptyItem(item)) {}
ItemStack[] compactedItems = ItemUtil.combineSimilarItems(items, respectMaxStackSize);
ItemStack saneItem = ItemUtil.makeEmptyItemAir(insaneSpigotApiItem /* See documentation */);
```

See more examples [here](https://github.com/KSashaDF/InventoryUtil/tree/main/src/examples/java).

## Using

Unfortunately, this library is not hosted on any public maven repositories. This means that the easiest way to use this library in your project is to download it. A pre-built jar can be found [here](https://github.com/KSashaDF/InventoryUtil/releases/latest).

### Adding File Dependencies to Gradle

Jar files (located in the \<project root>/libraries folder) can be added to a Gradle project by adding the following to the `build.gradle` file:

```groovy
dependencies {
    implementation(fileTree("libraries").matching {
        include("**/*.jar")
    })
}
```

### Adding GitHub Packages Dependencies to Gradle

While this project is not hosted on any public maven repositories, it is hosted on a private one. That is, GitHub Packages. Authentication is required to use artifacts hosted on GitHub Packages, despite the artifacts being public (see the "Packages" sub-heading on the right).

Add the following to the `build.gradle` file to add this library to your project:

```groovy
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/KSashaDF/InventoryUtil/")
        credentials {
            username = "GITHUB USERNAME"
            password = "PERSONAL ACCESS TOKEN"
        }
    }
}

dependencies {
    implementation("me.sashak:inventory-util:latest")
}
```

To generate a personal access token for your GitHub account, see [this page](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token). The token only needs the `read:packages` permission.

If you do not wish to have your username and token visible in the `build.gradle` file, you can put it in the `gradle.properties` file instead. Use `providers.gradleProperty("<property name>").get()` to get properties from the `gradle.properties` file.

See [this](https://stackoverflow.com/a/58453517) Stack Overflow answer for how to do this in a Maven project.