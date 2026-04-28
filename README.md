# COMP 345 Project 2.0 – Task Pitch

## Overview

This project is a **paper trading stock exchange platform** that allows users to practice trading stocks and ETFs using simulated money. The system uses both the Yahoo Finance API and the Twelve Data API to pull real-time stock data so users can research investments and manage a simulated portfolio.

For simplicity, the term **stock** is used throughout this document to refer to any investment.

We have a **CLI implementation** as well as a **web application.** The **web application** will be what we are demonstrating and focusing on.

---

## Core Features

### Stock Research
#### Researching Stocks
- Enter in a stock (including publicly-traded mutual funds or exchange-traded funds) symbol
- Will display:
    - Price history graph (Over 1 week, 1 month, 1 year)
    - Stock ticker and company name
    - Opening price
    - Latest price
    - Volume (how many stocks have been traded throughout the day)
    - Change (in dollars and by percent)
    - Performance (percentage change over 1 week, 1 month, 3 months, 6 months, year-to-date)
    - Stock analysis advice
- The user can then refresh the live price by refreshing the webpage

#### Purchasing Stocks
- On the same page displaying the above information, a stock can be purchased through **dollars and by number of shares**
- An estimated cost will be calculated when determining how much to buy
- The live price will be updated before the transaction is completed
- The transaction **must not exceed the current cash balance**

#### What-If Analysis
- Below the option to purchase a stock, the user can see what would have happened if they purchased the stock on a certain date
- The user selects how many shares they want to purchase, and the date to price the stock at
- This calculation then displays the original price at that date, versus the current price, alongside the change in value
- The stock will **not** be purchased for the user

#### Understanding Information
- Below all the technical analysis and ability to purchase stocks is a menu explaining
    - Frequently used metrics
    - How to read price charts
    - A glossary of definitions commonly used in investing and trading

### Portfolio Management
#### The Portfolio
- The portfolio displays all the investments a user has made, and shows line-by-line for each investment:
    - Stock symbol & name
    - Number of shares
    - Purchase date
    - Average cost
    - Current price
    - Value
    - Price change (percent)
- The portfolio also shows the user's current portfolio value, cash balance, and overall change in value

#### Selling Stocks
- Investments can be sold to increase the cash balance by the investment's value

#### Account Specifications
- Each user account has a distinct username, password, and portfolio
- Each user starts with **$10,000**
- Users may only gain or lose money beyond the initial $10,000 through **realized gains or losses** from selling stocks
- Since the money is simulated, there is no limit to how much a user can gain or lose

#### Account Management
- Individual users can create multiple accounts if they choose to start over
- All transaction and investment data is stored in a user's distinct json file

---

## Limitations

### API Limitations
- Stock data may be delayed or unavailable due to **API limitations**
    - The **Yahoo Finance API** is used for stock research and latest price data
    - The **Twelve Data API** is used for historical prices in the What-If Analysis feature
- All searches must be performed using the stock symbol

### Platform Limitations
- Users **cannot take on debt** to purchase stocks
    - They may only spend the cash they currently have
- Users must buy and sell at **market value only**
    - No stop-loss or stop-limit orders
- Only **publicly available companies and ETFs/mutual funds** may be traded
    - Commodities and currencies that are not tracked through an ETF/mutual fund are excluded

---

## Excluded Features
- No administrative accounts or management
    - As this platform is purely for trading practice, only user credentials are stored
    - Accounts can be created, but not deleted

## Sprint Information

---
### Sprint Diary
https://docs.google.com/document/d/1fMlQFu3eeY_bOj7huG8CzIbRopsYXZhz-vAS6GAQOIc

### Sprint 1 Review
https://docs.google.com/document/d/1kY0PO8JxcXUhLz7ybEKR8b8pzsUQnz9_IJyOBBCMe7w

### Sprint 1 Retrospective
https://docs.google.com/document/d/1Zoo7wAxc-k_x145A1b-dCt78e1h3ZQr2WWR8p_BQi0Q

### Sprint 2 Review
https://docs.google.com/document/d/1gkXGanp0KlpdXKhPZEXx28eHQ4JMBSvqu-arnf0j74M

## Diagrams

### Use Case Diagram

![Use Case Diagram](diagrams/use-case-diagram.png)

### Class Diagram
![Class Diagram](diagrams/class-diagram.png)

### Sell Stock Sequence Diagram
![Sell Sequence Diagram](diagrams/SellSequeneceDiagram.png)

### Research Stock Sequence Diagram
![Research Stock Sequence Diagram](diagrams/ResearchStockSequenceDiagram.png)

### Buying Stock Sequence Diagram
![Buying Stock Sequence Diagram](diagrams/BuyingStockSequenceDiagram.png)

## Coding Agent Usage

- We used coding agents (Claude and ChatGPT) to:
    - Create boilerplate code for the web application
    - Develop the HTML framework for the web application
    - Debug program
    - Write tests
    - Combine different branch functionalities