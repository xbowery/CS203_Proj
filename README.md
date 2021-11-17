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

### Description of problem

With the onset of the COVID-19 pandemic, the Food & Beverage (F&B) industry remains one of the most disrupted sectors in the economy. F&B businesses are often subjected to many rounds of closures as well as changing safe management restrictions and rules, resulting in the volatility of their businesses.
 
As such, F&B businesses have to quickly adapt to new changes and measures imposed by the Singapore Government, such as whether dining-in is allowed or not, and the maximum capacity allowed for dining-in. Additionally, employees will need to be managed accordingly, through routine administration of various mandatory PCR tests depending on their vaccination status. As the government strides towards achieving the new normal as part of our daily lives, there would inevitably be instances where employees are serving Quarantine Orders (QO) or Stay-Home Notices (SHN). Thus, we believe businesses would benefit should they be able to efficiently manage their human resources based on employees’ current statuses. 

Furthermore, many individuals face an uphill battle in keeping up with the latest news. Hence, our group chose to target F&B businesses and individuals specifically to smoothen their transition towards the new normal in Singapore.

### Description of our solution

Our web application, Swisshack, is a one-stop solution to help F&B businesses and consumers adapt to the changing safe management measures and restrictions. We have identified 3 target audiences for our application - F&B Business Owners, Employees, as well as normal users (patrons/consumers).

With changing safe management measures and updated restrictions as we gradually adapt to the new normal, every Swisshack user will be able to keep up to date with the latest news and government directives on F&B or COVID-19 related matters. 

For a business owner, Swisshack will be an easy-to-use platform for him/her to keep track of his/her employees' details from their vaccination statuses to COVID testing results. Swisshack also allows the business owner to review the overall crowd levels per day for his business for the past 7 days.

For an employee, he/she will also be able to manage his/her COVID testing schedule regularly and receive reminders to submit his self-swab test results if he/she has not completed them. Employees are also able to submit their COVID test results and review their COVID test history via Swisshack. 

Users will also be able to discover restaurants and their updated crowd levels in real-time through Swisshack. This will assist them to make a more informed decision whether to visit a restaurant depending on its current crowd level. This functionality will allow users to reduce their exposure to high crowd levels should they initially decide to patronize a restaurant with a huge crowd.

## Deployed application site

You may access the deployed site for our application [here](https://happy-stone-0f3668c00.azurestaticapps.net) (Currently the deployed site is taken down due to costs, but do contact us if you want to site to be re-deployed.)

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

Next, we are grateful for the various open source APIs which made our application possible. They include the [News API](https://newsapi.org/) service, [OneMap API](https://www.onemap.gov.sg/docs/) as well as [Covid-19 SITREP](https://covidsitrep.moh.gov.sg/) site for the various data.

Lastly, we would like to thank all the authors who made great blog posts and tutorials. They greatly helped us in our journey while creating this application.
