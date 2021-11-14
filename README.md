![Poster](https://user-images.githubusercontent.com/69230356/141650414-5cf1979d-ec6e-4a8a-ac87-74636b8287a8.png)

# Swisshack - Presented by G2T4

Group Project for SMU CS203 - Collaborative Software Development (G2T4)

## Group Members

- [Audrey Ang](https://github.com/audreyangg)
- [Cheah King Yeh](https://github.com/xbowery)
- [Jerome Ng Jian Jie](https://github.com/RandomBoiBoi)
- [Joseph Teo Jie Xian](https://github.com/josephishwite)
- [Won Ying Keat](https://github.com/wonyk)
- [Yuen Kah May](https://github.com/yuenkm40)

## Description

### Problem Overview

With the onset of the COVID-19 pandemic, the Food & Beverage (F&B) industry remains one of the most disrupted sectors in the economy. F&B businesses are often subjected to many rounds of closures as well as changing safe management restrictions and rules, resulting in the volatility of their businesses.
 
As such, businesses are required to quickly adapt to new changes and measures imposed by the Government, such as whether dine-in is allowed or not. Additionally, employees will need to be managed accordingly, through routine administration of various PCR tests depending on their vaccination status. As the government strides towards the new normal as part of our daily lives, there would inevitably be instances where employees are serving Quarantine Orders (QO) or Stay-Home Notices (SHN). Thus, we believe businesses would benefit should they be able to efficiently manage their human resources based on employees’ statuses. 

Furthermore, individuals face an uphill battle in keeping up with the latest news. Hence, our group chose to target **F&B businesses and individuals** specifically to smoothen their transition towards _the new normal_ in Singapore.

### Description of our solution

Our web application, Swisshack, would be a one-stop solution to help food and beverage businesses and consumers adapt to the changing safe management measures and restrictions. 

With changing safe management measures and restrictions as we gradually adapt to the new normal, users will be able to keep up to date with the latest news and government directives on food and beverage or COVID-19 related matters. Users would also be able to discover restaurants and their updated crowd levels in real time so that consumers like us would be informed of restaurants with high crowds to reduce exposure.

For a business owner, Swisshack will be an easy-to-use platform for him to keep track of his business’s employees from vaccination status to covid testing results or even review his weekly crowd levels.

Last but not the least, an employee will be able to manage his COVID testing schedules regularly and receive reminders to submit his self-swab test results if he has not completed them. 

## Deployed application site

You may access the deployed site for our application here:
https://happy-stone-0f3668c00.azurestaticapps.net/

## Running the application locally

### Pre-requisites

Please ensure that you have at least `JDK 11`, `Node v14.0.0` as well as `Maven 3.3` installed in your machine before running the application locally

### Steps to run the application locally

To run the application locally, please do the following steps:

1. Clone the repo<br>
Either by running the command `git clone https://github.com/xbowery/CS203_Proj.git` or cloning directly from Github Desktop

2. Open the `CS203_Proj` folder on your favourite IDE.

3. In the terminal of the IDE, run the following commands in order so as to start the backend:<br>
`cd APICode/`<br>
`./mvnw spring-boot:run` (for MacOS and Linux) or `mvnw spring-boot:run` (for Windows)
4. Open a new terminal and run the following commands in order so as to start the frontend:<br>
`cd FrontEnd/`<br>
`npm run prod`<br>

If you are running the application locally for the first time, please run the following command first:
`npm install -g @vue/cli`

5. Open another terminal and run the following commands to run the News Microservice:<br>
`cd NewsMicroservice/`<br>
`npm start` or `npm start windows` (if you are running the command on a Windows machine)

If you are running the application locally for the first time, please run the following command first:
`npm install`
 
The application should be running on `localhost:3000`.
