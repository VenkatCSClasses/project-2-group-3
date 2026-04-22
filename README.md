# COMP 345 Project 2.0 – Task Pitch

## Overview

This project is a **paper trading stock exchange platform** that allows users to practice trading stocks and ETFs using simulated money. The system uses a library and API to pull real-time stock data so users can research investments and manage a simulated portfolio.

For simplicity, the term **stock** is used throughout this document to refer to any investment.

The platform must support:

- Multiple investments in the same stock across different days
- Tracking each investment separately as its value changes over time
- Standard user accounts for trading and portfolio management
- Administrator accounts for account management and balance viewing

---

## Core Features

### User Features

After logging in, a user should see a menu with the following options:

#### View Portfolio
- View all current positions
- Support multiple investments in the same stock
- For each position, display:
    - Stock ticker
    - Company name
    - Last closing price
    - Last opening price
    - Amount invested in:
        - dollars
        - shares
    - Percentage change from the initial investment
- Display:
    - Total portfolio value
    - Total portfolio percentage change

#### Manage Portfolio
- Similar to **View Portfolio**
- Allows the user to buy or sell stocks while viewing their portfolio

#### Buy / Sell Stocks
- Allows the user to buy or sell stocks directly

#### Research Stocks
- Allows the user to search by stock symbol
- Pulls and displays real-time stock data

#### Exit
- Exits the application

---

## Research Stocks Requirements

At minimum, the **Research Stocks** feature must display:

- Ticker
- Company name
- Last closing price
- Last opening price
- Price and percentage change over:
    - 1 day
    - 1 week
    - 1 month
    - 3 months
    - 6 months
    - year-to-date
- Volume

---

## Account Specifications

- Each user account must have:
    - a distinct user ID
    - a password
- Each user starts with **$100,000**
- Users may only gain or lose money beyond the initial $100,000 through **realized gains or losses** from selling stocks
- Since the money is simulated, there is no limit to how much a user can gain or lose

---

## Trading Rules

- Users can purchase stocks by:
    - pure dollar value
    - number of shares
- When selling stocks, users can choose to sell:
    - part of their holdings by dollar amount
    - part of their holdings by number of shares
    - their entire stake

---

## Administrator Features

Administrators can:

- View the balances of each account
- Create accounts
- Close accounts

Administrators **cannot** view user investments.

---

## Limitations

- Real-time stock data should be updated **at least once per day**
    - Exact update frequency depends on the library and API used
- Users cannot take on debt to purchase stocks
    - They may only spend the cash they currently have
- Users must buy and sell at **market value only**
    - No stop-loss or stop-limit orders
- Only **publicly available companies and ETFs** may be traded
    - Commodities and currencies are excluded

---
## Sprint Diary
https://docs.google.com/document/d/1fMlQFu3eeY_bOj7huG8CzIbRopsYXZhz-vAS6GAQOIc

## Sprint 1 Review
https://docs.google.com/document/d/1kY0PO8JxcXUhLz7ybEKR8b8pzsUQnz9_IJyOBBCMe7w

## Sprint 1 Retrospective
https://docs.google.com/document/d/1Zoo7wAxc-k_x145A1b-dCt78e1h3ZQr2WWR8p_BQi0Q

## Sprint 2 Review
https://docs.google.com/document/d/1gkXGanp0KlpdXKhPZEXx28eHQ4JMBSvqu-arnf0j74M

## Use Case Diagram

![Use Case Diagram](diagrams/use-case-diagram.png)

## Class Diagram

![Use Case Diagram](diagrams/class-diagram.png)
