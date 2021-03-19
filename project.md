---
class: Mobile and Ubiquitous Computing
term: 2020/21
skill: Mobile Application Development for Android
title: "ShopIST: Keeping Track of Groceries"
author:
  - Prof. Luis D. Pedrosa
  - Prof. João C. Garcia
assignment: Class Project
issued: 2021-03-19
checkpoint: 2021-04-23
due: 2021-05-18
version: 1.0
---


# Overview

In this project you will learn how to develop a non-trivial mobile application for the Android Operating System.
This application will explore several key aspects to mobile development, including location awareness, judicious use of limited resources, and social behavior.
Towards this end you will be developing a simple App to help people manage and keep track of their groceries: ShopIST.

The goal of the ShopIST application is to help people manage their groceries, by keeping track of an inventory of everyday home items available in the pantry and in need of being purchased.
Users can create lists that show these items, while keeping track of how many are available in the pantry, how many the user needs to purchase, and also how many are currently in the shopping cart when the user is in the store.
As the user consumes items at home, they can select the item from the list and update its status by deducting the amount available and increasing the amount in need.
Later, while at the supermarket, the user can view which items they need which are available at that store and progressively move items to their cart by deducting the amount in need and increasing the amount in the cart, allowing them to keep track of what they have already picked and what is already in the cart.
Once shopping is complete, the contents of the cart can be moved back to the pantry, keeping the inventory up to date.
This entire inventory management process can be synchronized across multiple devices and users, allowing a family to collaboratively track inventory and shopping lists as different people consume and purchase items.
The app will also include advanced features to crowd-source useful information for the user, including item pricing, pictures, and checkout queue wait times.

The project will be built as the sum of a set of [mandatory](#mandatory-features) and [additional](#additional-components) components and features.
The idea is that everyone implements the mandatory features (worth up to 75% of the grade), and then selects a combination of additional features that add up to complete the grade at 100%.
Grades from the optional features are allowed to accumulate beyond 25% (and compensate for any limitations in the mandatory part of the submission), though the final grade is capped at 100%.

# Mandatory Features
As the user opens the application, they should be presented with a series of pantry lists and store shopping lists that they have registered from within the app.
Pantry lists keep track of items, as described earlier, and store shopping lists collate and filter data from all pantry lists, providing a combined view of everything the user needs to purchase and filtering it down to what is sold at the particular store.
All of these lists may be associated with a location (e.g. by inputting an address, picking a point on a map, or by using the current location).
This location can then be used to automatically open a specific list when the app is opened, if the user is currently located close to the associated location.
This way, the app will quickly direct the user to a specific supermarket's view, or to their home pantry list if they are home.
If unsure about the user's location, or which list or store to select, default to displaying everything and always let the user override the decision and to open other lists, regardless of their location.

When displaying information about lists (both pantry lists and store shopping lists), in addition to their names, also show other useful information, like the number of items in the list, the drive time to get to the list's location (using the Google Directions API or equivalent), and the estimated queuing time at checkout (calculated as described [below](#queue-wait-time-estimate)).

When opening a list, the user should be able to review its location on a map and request directions to get to it.
The user should also see the list of items itself, with their respective quantities.
Options should be readily available for adding new or known items to a list, either through manual entry of the item's meta-data, or by scanning the item's bar code[^barcode] to quickly recognize it from previous uses.

In addition to its name and potentially its barcode number, each item may also have one or more optional pictures, supplied by the user either via a file or a quick snap of the camera.
By default, show the user the first picture as a thumbnail, but let them view more photos by scrolling or clicking for more.
Clicking on a thumbnail should show the picture full screen, so the user can see more clearly.
You should also let the user indicate which stores carry the item (so the store shopping list can filter the item later) and allow the user to keep track of recently seen prices for the item at each store.

[^barcode]: Feel free to use a publicly available barcode scanning library, no need to implement the decoding algorithm yourself.

With that in mind, we leave the exact UI design up to you, but the app should support a usable workflow, based on common use cases:

* Support multiple pantry lists and multiple store shopping lists.
  Pantry lists track the individual items, including the quantities that are available in the pantry, in need of being purchased, and currently in the cart.
  Store shopping lists act like "views" combining and filtering data from the pantry lists.
  If, e.g. a user needs to buy 2 loaves of bread for themselves and 1 for a family member (relating to separate pantry lists), a supermarket's shopping list should indicate 3 loaves of bread and a hardware store should omit the bread altogether.
* When in a pantry list, the user can select an item (either from the list, or by scanning its barcode) and "move" it to the shopping list.
  Allow the user to also indicate a quantity so, if, e.g. they have 5 cans of beans and eat 1 of them, 1 will show in the shopping list, while 4 remain available in the pantry.
* When in a specific store shopping list, only show the items available in that store.
  Include, alongside the product name, the quantity needed and its price at the given store.
* While in a shopping list, allow the user to "move" an item to their cart.
  As before, allow them to indicate a quantity, as the store may not have all that they wanted.
  Make it easy to understand what the user still needs to pick in the store and what they already have in their cart.
  Consider the case where the item may be related to more than one pantry list, in which case the user may need to indicate how much they want to assign to each.
* Allow the user to track their cart, including the number of items and their total cost.
* Upon completing their shopping, allow the user to quickly "move" the contents of the cart to the respective pantries, thus making it easy to track inventory.
* Mistakes can happen and users can accidentally tap the wrong thing.
  Allow the user to rectify any mistakes by leaving open the option to "move" an item from one list to another even if that is not the usual use case scenario.
* Make common operations quick and easy, using simple gestures like tapping and swiping.
  Less common operations may use more complex gestures like a long tap, or menus.

The app should also support a number of features which build on explicit data sharing and crowd-sourced data:

* The user should be able to share lists with other users.
  This allows the user to manage their shopping across multiple devices they own, as well as with other people, e.g. within their family.
  List synchronization should be mediated via a central server and can be initially set up by exchanging e.g. short alphanumeric codes or a QR code.
* The operation of shared lists across multiple devices should be seamless.
  If one user indicates that an item needs to be purchased in a shared pantry list, another synced user should be able to purchase the item in the store.
* When the user adds a picture to an item, or indicates its price at a specific store, this should be shared with a central server that allows all other users to consult that data as well and to use it as it updates.
  Use the barcode to identify items and location similarity to identify stores, regardless of their names.
  This data is all collected via crowd-sourcing, no authoritative databases will be used.
* Users share the point in time when they arrive at the checkout queue and when they clear it to help crowd-source a model to predict wait times.
  This model should also be built and shared in the central server, for all to benefit.
* Syncing lists and collecting crowd-sourced data implies retrieving data from a central-server, which can use up expensive network data.
  Use a cache to optimize the process by keeping a local copy of all downloaded data so that the app may still function with outdated data if the network goes down and to avoid re-downloading large resources like item pictures.
  See [below](#caching) how the caching algorithm should work.
\newpage
* Data sharing and crowd-sourcing is coordinated by a central server that is not the focus of this class but is nonetheless necessary for the project to work.
  Feel free to implement your own server as you see fit (e.g. using standard TCP sockets and Java Object Serialization, or gRPC), or otherwise use an existing server software or database.
  The server can be kept simple for debug and development purposes and can hold data in memory alone (no need for persistence).
  In any case, be prepared to justify your choices.


## Queue Wait Time Estimate

Estimating the amount of time a user may expect to wait in queue at checkout can be a challenge.
One way to address this is to monitor users as they arrive and leave from the queuing area to infer the wait time for each individual.
This wait time, on its own, isn't immediately useful, as it can only be computed once the user has left the queue, but it can be used for a basic learning algorithm.
Another useful metric is the cumulative amount of items in the carts of users that are currently waiting in the queuing area.
Assuming a constant queuing service rate (each item takes about the same amount of time to be scanned at checkout), the estimated queue length (the sum of the amount of items in the carts of people in the checkout area) can be used to infer the wait time, while also being measurable in real-time.

This is the approach that we will be exploring in this project.
We will assume the store has installed in the checkout area a transmitter beacon.
In the real world, this can pose challenges, as these approaches need to assume that the beacon is carefully placed so that only those waiting in queue can detect the beacon and anyone otherwise walking around the store is out of range.
In practice, we will be emulating these beacons in software and therefore such concerns go beyond the scope of the project.

Transmitter beacons are small simple devices that periodically broadcast a packet identifying themselves, typically using the Bluetooth Low Energy (BLE) protocol stack.
Our in-house mobility emulator (Termite) only supports WiFi Direct, so we will be using that protocol instead.
Though their APIs may differ slightly, both kinds of beacons operate in a similar fashion so this should not significantly impact development.

The beacons themselves do not detect passersby but a user with an application that is aware of the beacons can use its proximity to trigger an event.
In our case, this means that we cannot accurately infer how many people are waiting in queue, just how many of them have ShopIST installed.
This is not a problem if we assume an even distribution of ShopIST users within the crowd.
As such we will be inferring the queue time, based on the number of ShopIST users in queue.

Towards this end, the ShopIST application tracks when the phone comes in proximity to a known beacon and when it has left (it has not heard the beacon in a while).
This information is reported to the central server which calculates how long each user waited in queue, and how many people were in the vicinity of the beacon at each point in time.
After enough data is collected, a simple linear regression can be used to infer how long the wait is, given how many users are currently close to the beacon.
This learning process should be done independently for each store, as the queue service time, as well as the distribution of ShopIST users may vary between them.


## Caching
Most of the data shared between the ShopIST application and its central server is small (short text strings) and time sensitive (I want the queue wait time now, not an hour ago).
That said, not all data fits this pattern.
Pictures, e.g. are much larger, which can be taxing on a metered data connection, and are also not necessarily of a timely nature.
Mobile data connections can also be unstable, which can make it challenging to operate an app with spotty Internet.
This makes caching particularly important, with large data being pre-loaded, when possible, over WiFi.

Towards this end, the ShopIST application should build a local cache for all data it may need from the central server.
Small timely data can bypass the cache, to check for updates, but should fallback to the cache if the network is down.

Larger data, namely user pictures, should be handled more judiciously.
Whenever a picture becomes visible in the user interface, the application should first check the cache and only if the image is not present should it then download the data from the server.
Once downloaded, the photo should remain in cache, in case it is needed again.
The cache size should be capped at 10MB (for testing purposes) and as new downloads exceed that quota, older items should be evicted (following a "least recently used" or LRU policy).

Furthermore, a cache aware WiFi pre-loading mechanism should be put in place that downloads the first picture for each item, whenever the user connects to a WiFi network.
With all of this in place, a user that just looks up an item and doesn't scroll or tap to check for other pictures may never need to download a photo over their metered data connection.


# Additional Components
In this section we list a series of features that can be combined to add value to ShopIST.
Each feature lists the grading percentage it is worth, reflecting the relative difficulty in implementing it.

The project core (described [above](#mandatory-features)) is worth 75%, which leaves at least 25% for these additional components.
Select a combination of these features that adds up to or exceeds that threshold.

Many of these features can be naturally integrated with each other and gain from being implemented in such a manner.
Implementing your selection of additional features in such a cohesive manner is encouraged.



## Securing Communication [5%]
Sending data across a network in the clear poses a significant risk to users.
A malicious party can eavesdrop the data being transmitted and use it to infer all sorts of private information, including e.g. a user's location.
Unprotected transmissions can also be modified in transit, feeding users fake data that can influence their decisions (e.g. make them buy something they do not need) or even put them in danger (give them directions to the middle of a construction site).

To avoid such scenarios, upgrade the ShopIST infrastructure to secure all communications between the mobile application and the back-end server to use SSL.
Also, do be careful with how you manage your certificates to be sure the client only communicates with your authorized ShopIST server, preventing an untrusted party from conducting a man-in-the-middle attack.


## Meta Moderation [10%]
Another way to attack the integrity of ShopIST data is for the attacker to simulate one or more valid users and to then simply feed fake data into the system.
There is no easy way to completely solve this problem but a meta-moderation system raises the bar.

Create a mechanism whereby users not only submit data (pictures, prices, etc.), but can also flag them as inauthentic.
Whenever a user-submitted data item is brought to main focus (the item information screen, or a photo shown in full screen), add the option to flag it.
As items get flagged, keep track of the trustworthiness of individual items and also the users who submitted them.
Use the trustworthiness of a user's recent submissions to set the initial trustworthiness of any new data they submit.

When showing user-supplied data, sort it by its trustworthiness and/or filter out any data below a threshold.
This way, useful data percolates to the top making it more likely to be seen.


## User Ratings [5%]
To help users find better products to purchase, create a rating system that lets users rate individual items.
Add the option for users to rate items on a 5-star system in the item information screen.
Keep track of this data, along with other meta-data, like item pictures and pricing.
Whenever the user opens up the item information screen, namely by scanning the barcode, show the average star rating.
Also include a more detailed breakdown of user ratings (e.g. as a histogram).


## User Accounts [10%]
The project [core](#mandatory-features) does not include a login/logout procedure so users can be tracked via simple GUIDs.
This simplifies the user experience, as users need not setup an account before using ShopIST but it also makes it more difficult to share data and to keep multiple devices consistent.

Allow users to create accounts to make sharing and syncing easier.
Include Login/Logout buttons in the context menu.
Multiple devices logged in for the same user should automatically sync the same lists, providing a seamless cross-device experience.
Also allow users to share lists by adding and removing other users via a simple access control list.
User accounts should be optional, so users can start using the app without an explicit account and later upgrade to using an account if they want.


## Social Sharing [5%]
Another important aspect in mobile development is social sharing.
It's often convenient to share information with friends in context and directly from within an application, without needing to open a communication app to do so.

Add the option to share a list item from its information panel.
This should allow users to use common social media (e.g. Facebook, Twitter) and communication applications (E-Mail) to recommend a product to a friend.


## Optimize the Shopping Process [10%]
The [core application](#mandatory-features) already knows what the user needs to buy, where it is available, and how much it costs at each location.
This information, if combined, can be used to significantly optimize the user's shopping experience.

Add an option to the main screen to plan the user's shopping.
When the user selects this option, build a complete plan to buy all items they need, telling them which stores to go to in sequence and filtering the products in the respective shopping lists so they know what to buy where.

Let the user indicate whether they wish to optimize for time or cost.
When prioritizing for time, consider the driving time from the users current location to the first store and between any subsequent stores.
Also consider the estimated wait time at checkout.
With that in mind, select a sequence of stores that minimizes the sum of these times.
When optimizing cost, take into consideration the cost of each item at each store and prepare a sequence of stores that minimizes these costs while also minimizing travel time to and from each store.
Use reasonable approximations to solving the traveling salesperson problem.


## User Prompts [5%]
Crowd-sourcing is a great way to collect large amounts of data and to tap into the wisdom of the crowds.
Unfortunately bootstrapping the process can be a challenge as users often contribute more when more data is already available.

To nudge users towards submitting more data on ShopIST, implement a notification that prompts the user to submit data when it is most timely.
Particularly, prompt the user to submit missing price data or pictures when they scan its barcode or put it in their cart while at a store (from within its respective shopping list).
Use as much information about the user's current context as possible to offer relevant defaults for any submitted data values.


## Localization [5%]
Users are diverse and multi-lingual and ShopIST should reflect that.
Localization, also called L10n, is a collection of tools and techniques that allow different users that speak different languages to use the application and share data without barriers.

Translate all static strings presented to the user into at least two languages (e.g. English and your own native language) and store these strings in such a way that adding new translations is easy and does not require refactoring the application.
By default, use the operating system's configured language, but also let users override this configuration within the app itself and store their preference.

A database of static strings works well for menus and buttons, but cannot handle user submitted content.
Use the Google Translate API, or equivalent, to translate all user submitted text to their chosen language.
When showing text like this in an information panel, clearly indicate that the new text is a translation and add the option to show/hide the original.


## Suggestions [10%]
Often users will consume and purchase certain items in pairs.
Being able to detect these patterns and suggesting that the user add the second item to the shopping list when they add the first can improve user experience in using the app and reduce forgetfulness.
Crowd-source which products-pairs are most likely to be purchased together.
If a given product pair shows up often enough to no longer be a coincidence, automatically prompt the user to add the second item to the shopping list when they add the first.
If well designed, this feature can both suggest something the user would have already done, or also recommend relevant product pairs that the users might not have thought of on their own.


## Smart Sorting [10%]
Typically users will pick items in a store following a similar order in each visit and it would help if the shopping list were already ordered that way, to reduce scrolling around through a long list.
As the user zigzags through each aisle, starting from the entrance and making their way to the checkout area, they will tend to find the same products in the same order.

As users move items to their carts in a given store (from within the store's specific list), keep track of which items were placed before each other more often (you can track this as a matrix that counts how often each product was placed in the cart before each other product).
Crowd-source this data from all users, but track it separately for each store, as different stores will organize their products differently.
Once you have collected this data, sort the shopping list accordingly, i.e. use the data to build a custom comparator to use in the sorting algorithm.
If done well, and once sufficient data has been collected, the next item the user is about to pick will always be close to the top of the list and shopping should be as easy as going down the list top to bottom.


# Alternative Projects
Groups have the option to develop a different project if they so wish and with faculty approval.
Consider this option if you already had a project in mind, whether for some collaboration with industry, to explore an idea for a start-up, or as a passion project.
We might need to make adjustments to manage complexity, given the time-frame and effort allocated to the course.
There are also some features that we consider essential to mobile development and we may need to add or adjust functionality to your idea for the sake of the class project.
Start a discussion with the faculty and seek approval as early as possible if you wish to try this option.


\newpage
# Grading Process and Milestones
Projects will be evaluated on a variety of dimensions.
The most important factors are: the degree to which baseline and additional features are implemented, how well integrated the features are, the technical quality of algorithms and protocols, and resource efficiency decisions.
We will also assess the responsiveness and intuitiveness of the application interface.
This is not a course in graphic design so, beyond basic utility and effectiveness, GUI aesthetics will not be graded.
Do not invest too much time in creating pretty icons or other superficial assets and focus on making information accessible to the user.
Consider using Unicode characters in place of hand drawn icons, when appropriate.

There are two important project milestones: 

April 23^rd^: Project Checkpoint
: Students should submit their current prototype so that they can receive feedback on the architecture and status of the prototype.
  Try to have most of the [mandatory functionality](#mandatory-features) ready, as well as a plan for the set of [additional features](#additional-components) you intend to implement.
  Consider including an Activity Wireframe and a prepared list of questions to focus the feedback.

May 18^th^: Project Submission
: Students should submit a fully functional prototype and a final report.
  All source code and the report must be submitted through the course Fénix website.
  A template of the report will be published on the website.
  The report must describe what was and was not implemented, and it is limited to 5 pages (not including the cover).
  The cover must indicate the group number, and the name and student ID number for each of the group's elements.

# Collaboration, Fraud and Group Grading
Student groups are allowed and encouraged to discuss their project's technical solutions without showing, sharing, or copying code with other groups or any other person, whether attending the course or otherwise.
Fraud detection tools will be used to detect code similarities.
Instances of fraud will disqualify the groups involved and will be reported to the relevant authorities.
Furthermore, within each group all students are expected to have a working knowledge of the entire project's code.
