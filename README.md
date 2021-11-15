# Swisshack - Presented by G2T4

![Poster](https://user-images.githubusercontent.com/69230356/141650414-5cf1979d-ec6e-4a8a-ac87-74636b8287a8.png)

[![Backend Java CI](https://github.com/xbowery/CS203_Proj/actions/workflows/apiCode_CI.yml/badge.svg)](https://github.com/xbowery/CS203_Proj/actions/workflows/apiCode_CI.yml)
[![News API CI](https://github.com/xbowery/CS203_Proj/actions/workflows/news_CI.yml/badge.svg)](https://github.com/xbowery/CS203_Proj/actions/workflows/news_CI.yml)
[![Vue CI](https://github.com/xbowery/CS203_Proj/actions/workflows/vue_ci.yml/badge.svg)](https://github.com/xbowery/CS203_Proj/actions/workflows/vue_ci.yml)

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

You may access the deployed site for our application [here](https://happy-stone-0f3668c00.azurestaticapps.net)

## Running the application locally

### Pre-requisites

Please ensure that you have `JDK 11+`, `Node v14+` as well as `Maven 3.3` installed in your machine before running the application locally

### Steps to run the application for development

To run the application locally, please do the following steps:

```bash
# Clone the repo directly or from GitHub Desktop
git clone https://github.com/xbowery/CS203_Proj.git

cd CS203_Proj/

# Open the folder in your favourite IDE. If you are using VSCode:
code .

# For the Java backend
cd APICode/

# For MacOS and Linux
./mvnw spring-boot:run 

# For windows OS
mvnw spring-boot:run

# For VueJS front-end code. Run on new terminal window:
cd FrontEnd/

# If you are running the application locally for the first time, please run the following command first:
npm install -g @vue/cli

npm install
npm run serve

# For news microservice. Run on new terminal window:
cd NewsMicroservice/

# For non-windows users
npm start 

# For windows users
npm start windows

# For running RestaurantMapper, open up a new terminal window
cd RestaurantMapper
npm install

# Create a .env file and fill in the necessary values based on the .env.example file
cp .env.example .env

# For non-windows users
npm start 

# For windows users
npm start windows
```

4 different applications should be running on port 3000, 3001, 3002 and 8080

## Running production code

Please visit the various READMEs in the respective subdirectories for detailed instructions on how to run the production code bases.

## Acknowledgements

We would like to thank our CS203 professor, Mr Tan Pang Jin, for his guidance and patience. This project would definitely not be possible without his teachings and advice.

Next, we are grateful for the various APIs which made our application possible. They include the [News API](https://newsapi.org/) service, [OneMap API](https://www.onemap.gov.sg/docs/) as well as [Covid-19 SITREP](https://covidsitrep.moh.gov.sg/) site for the various data.

Lastly, we would like to thank all the strangers and authors who made great blog posts and tutorials. They greatly helped us in our journey while creating this application.
