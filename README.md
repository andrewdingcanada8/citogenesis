# cs0320 Term Project 2020

**Team Members:**
Jason Gong, Charles Shi, Seiji Shaw and Andrew Ding

**Team Member Strengths and Weaknesses:**

**Andrew Ding**

Strengths:
- Likes to work with generic code and abstraction
- Open minded with different styled implementations & working styles
- Comments on functions and code

Weaknesses:
- Potential to create early optimizations and difficult to read code
- Weaker with SQL and using spark / html & css

**Jason Gong**

Strengths:
- Knows a bit about visual aesthetics
- Can be creative with code structuring (fresh ideas)

Weaknesses:
- Cannot catch edge cases for his life
Can be creative with code structuring (confusing for others)

**Seiji Shaw**

Strengths:
- Algorithms implementation
- Good at assessing runtime complexity and writing efficient implementations 
- Modular software architecture design

Weaknesses:
- HTML, Spark, CSS, Javascript

**Charles Shi**

Strengths:
- Likes to outline the class structures and methods first
- Practices incremental testing

Weaknesses:
- Could improve coding speed

**Project Idea(s):**

### Idea 1: LunchDate

Meeting up with friends in college is hard. Grabbing a meal together can be even harder. Scheduling conflicts, differing price sensitivity and the dizzying amount of choices. Enter LunchDate. The socially driven lunch group organizer that helps you find people to eat by showing you people’s availability and allowing for users to send invitations. The best part? You even get a discount for bringing a large group of people to the restaurants. Students can receive discounts if their group size is 3 or above, generating large amounts of traffic for restaurants everywhere.

**Critical Features for MF**

**User login:**
Users would have to log-in in order to modify their data and group options. This is an important feature because of privacy and security reasons. We wouldn’t want users to be able to change the friends and lunch groups of other users.

This may be a hard implementation because it would require us to implement some kind of secure login system. We are not quite sure how to make this section yet. I am predicting that we’d need to implement some form of SQL database with all usernames and passwords, as well as some form of encryption on the front and backend so that passwords are never stored as plain textfiles.

**User and restaurant profile database:**
In order to keep track of the lunch dates, the app must know what users there are, and which one of them are in a “lunch date” together at what restaurant.

Edge cases like accessing the same profile on multiple browsers might be hard to deal with. We must also address

We would also need to keep track of each user’s entire group of friends and make sure the friendship goes both ways.

**Restaurant Browsing and Discounts:**
Users would need to be able to see exactly what restaurants are available and what discounts they have. My friend Jake tells me that though Snackpass offers discounts as well, it’s not always super visible which restaurants are doing promotions.

This section is very frontend heavy, and we would need to incorporate many javascript elements to allow for custom clickable buttons, image galleries and live updates on the status of restaurants.

**QR code generation for restaurants to verify group date for discount:**
Restaurants will need to verify the validity of a group’s lunchdate by scanning a qr code on a user’s app. This QR code will need to be generated by us and scanned to confirm the validity. We want this feature because restaurants we’ve talked to have shown scepticism in the app because they think they can be “scammed” by customers who “claim” they are using the app and will give out unnecessary discounts.

This section will be hard because there will be cryptography elements to the algorithm for generating a scannable QR code. We will also need to use some form of API for scanning and decoding QR codes once we’ve generated them.

**Bells and Whistles**
**User availability and discovery:**
Users will be able to discover which of their friends is able to eat at what time.

This may be hard because it requires storing each user’s availability periods and finding intersects with the user’s friends. There will also need to be an algorithm to find the best times of the week to eat given a selected number of friends.

Rejected - a lot of good features but a core algorithm is lacking

### Idea 2: Citogenesis

Circular reporting, informally known as citogenesis, occurs when a work cites multiple sources that ultimately stem from a single source. While often used as a technique to justify and spread fake news and propaganda, this phenomenon also occurs naturally due to a lack of quality-control of sources on the internet. In particular, Wikipedia has exhibited a cyclical sort of circular reporting that occurs in two stages:
1. A user (regardless of intent) writes a false statement on Wikipedia
2. Another external source reads Wikipedia entry and writes an article without citing Wikipedia
3. Moderator on Wikipedia attaches source produced in step (2) to false statement produced in step (1)

Misinformation introduced this way has spread to major news outlets, popular television channels, and even academic publications. We intend to develop a tool that will (1) combat citogenesis on Wikipedia and (2) evaluate the overall reliability of a citation. 

Our tool will be a proxy Wikipedia webpage that will annotate each cited statement with a rating of the strength of its sources. To assess the viability of a statement, we intend to run a search on its citations to understand the total number unique supporting sources linked to the statement (including the sources of cited citations as well). This will amount to a graph-search on web pages, where each node is represented by a source page, and each edge established by a source citation. 
We also intend to leverage the Wikipedia API to access the editing history of the user’s desired Wikipedia article. By ensuring a sufficient number of cited sources are published before the editing/insertion date of the statement on Wikipedia, we can ensure report any instances of the cyclical circular reporting discussed above.

Main challenges:
There may be a chance that citations may not provide specific-enough information to uniquely identify the document if there is no hyperlink attached. This may be solved by using Google’s Search Engine API to make our best guess at the citation’s referenced source. 

Working with citations only may cut off available relevant sources to a particular statement. Using a common text-matching search engine (again, Google’s API) may be helpful, but it will be difficult to determine the reliability of a source that is delivered by a search algorithm.

**Critical Features**

**Proxy webpage:**
Since we do not intend to modify nor access Wikipedia’s codebase, we must find an alternate way to annotate Wikipedia’s webpages. While developing a browser plugin is a possibility, we would like this project to be platform-independent to be as accessible as possible to all users. Thus, we decided implementing this project as a proxy-webpage that can annotate Wikipedia pages would be best.

Challenges: 

- learning Wikipedia’s page API 
- figuring out how to change webpage HTML to support statement annotations

These problems can be both solved by dedicating the time to learn Wikipedia’s interface.

**Filter out false statements:**
Users have indicated that they would like to read a version of a Wikipedia page that is totally factually correct. Thus, this feature involves setting an “original source threshold” to filter out poorly-supported statements.

Challenges:

- Filtering out statements without compromising typical Wikipedia page format

We are not bound to Wikipedia’s page layout to present the filtered information. Thus, designing a flexible page format that can is readable regardless of the number of statements may be a solution to this problem.

**Highlight statements based-on how well they are supported by their sources:**
Users indicate that they would not like to mouse over every statement to check the validity of their sources. Highlighting is a quick way of giving visual feedback without any cumbersome mouse movements and/or clicks.

Challenges:

- The algorithm must be fast enough to annotate each statement by the time the user has to read it. Since many users skim Wikipedia articles quickly, this must be within the first few seconds of page load. 

This is heavily dependent on the speed of the internet connection of the server, as well as efficiency of the algorithm implementation. Writing in a timeout for long-pending HTTP requests may be necessary to achieve the response times needed to make this annotation usable.

Approved - very original with lots of room for design choices

### Idea 3: Steam Birds

Steambirds is an turn-based strategy game inspired by the dogfights (air-to-air battles) of the 20th century. In this game, you control a set of planes and try to maneuver behind the opponent’s planes in order to take them down.

While the original game is pretty good, it does lack a few features. For one, there is no online multiplayer option available for this game. The original only allows the users to play against an AI. For another, the original was made using Adobe Flash, an older program which is currently being phased out by most browsers. As a result, the joy that is Steambirds will soon be very difficult to access for the common public. With this project, we hope to recreate and improve Steambirds in order to ensure that the future generation can enjoy the same experience.

**Critical Features**

**Trajectory Calculation:**
Players pick their own maneuvers (in particular, direction and speed)
This will have to be rendered visually, displaying a line representing the hypothetical trajectory of the plane. Such will probably require some form of graphing-rendering system (polar graphing)?

**Turn Based Damage:**
Player presses “next turn”, and the player and AI’s planes move as planned. Damage is calculated based on the amount of time during which a plane is facing another plane and in range (the requirements for damaging the opponent’s plane)
One challenge with this turn will be to animate the frame-by-frame movements of the planes, including getting hits on and eliminating opponents from play
Another challenge will be the calculations for these animations. At what time periods can one plane open fire on the other?

**Online play:**
I want to pew-pew my friends! As seen with the recent rise of massive online games, multiplayer is a huge market and way to drive interest in our game.
The challenge with this will be to arrange the connection and make sure that a synchronization error does not occur. I would suggest hosting the “server” on the match host’s side, since I don’t think anyone on my team runs a server farm.

**Visual Design:**
It’s a game that needs to look good, as people will not like a bad design. We've polled some underclassmen and they said one of their top reasons for liking a game was visual design.
This means that we will have to work a lot on rendering

Approved - main focus should be on AI and multiplayer components

Note: you do not need to resubmit a final project idea. 

**Mentor TA:** _Put your mentor TA's name and email here once you're assigned one!_

## Meetings
_On your first meeting with your mentor TA, you should plan dates for at least the following meetings:_

**Specs, Mockup, and Design Meeting:** _(Schedule for on or before March 13)_

**4-Way Checkpoint:** _(Schedule for on or before April 23)_

**Adversary Checkpoint:** _(Schedule for on or before April 29 once you are assigned an adversary TA)_

## How to Build and Run
_A necessary part of any README!_
