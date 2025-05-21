# 📖 Sentiment-Based Bible Verse Recommender 🙏

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Awesome](https://cdn.rawgit.com/sindresorhus/awesome/d7305f38d29fed78fa85652e3a63e154dd8e8829/media/badge.svg)](https://github.com/sindresorhus/awesome)

## ✨ Overview

This project is a web application that provides uplifting Bible verses based on the sentiment of your input. Feeling down? Tell it how you feel, and it will suggest verses to encourage you! Think of it as a digital hug from scripture. 🤗

* **Frontend:** Built with Angular and TypeScript, providing a user-friendly interface.
* **Backend:** Developed using Java Spring Boot, handling sentiment analysis and verse recommendations.

## 🚀 Features

* **Sentiment Analysis:** Analyzes user input to determine the prevailing emotion.
* **Verse Recommendations:** Suggests relevant Bible verses based on the detected sentiment.
* **User-Friendly Interface:** Easy to use and navigate.
* **AWS Hosted**: Deployed with Amazone Web Services.

## 🗂️ Project Structure

The repository is organized into two main folders:

* **`backendJournal`:** Contains the Java Spring Boot backend code.
* **`frontendJournal`:** Contains the Angular frontend code.

## ⚙️ Technologies Used

* **Frontend:**
* Angular
* TypeScript
* **Backend:**
* Java
* Spring Boot
* **Sentiment Analysis:**
* IMDB watsonx.ai
* **Database:**
* MySQL
* AWS RDS
* **Cloud Hosting:**
* AWS S3
* AWS CloudFront
* AWS EC2

## ☁️ AWS Architecture

I have hosted the app on aws and diagram is illustrated below

![AWS Architecture Diagram](https://github.com/HongVoDev/Java-Angular_the-word-project/blob/6a75425a2d32adc98fdd7d0fab8e6b1184911f8f/thewordproject.PNG)

## ☁️ Demo

![Quick demo gif](https://github.com/HongVoDev/Java-Angular_the-word-project/blob/e4565854ecc842838a17bea2bb2febb87aec9cb6/word.gif)

## 🛠️ Local Setup

Follow these steps to run the project locally:

### 1. CORS Configuration 🛡️

For security reasons, I've removed the CORS configuration from the main code. However, I'll provide a sample CORS configuration on the **Wiki** page. You'll need to configure CORS in your local environment to allow the frontend to communicate with the backend.

### 2. Database Setup 💾

The backend requires a MySQL database. I've used AWS RDS to host the database. Details on how to create and configure the database, along with some sample data insertions, will be available on the **Wiki** page.

### 3. Frontend Setup 💻

You can copy the `src` and `public` folders from the `frontendJournal` directory into your own Angular project skeleton. Instructions on creating an Angular project can be found on the **Wiki** page.

### 4. Backend Setup ☕

Import the `backendJournal` folder in any Java IDE such as eclipse or intelliJ and run it

## 🤔 Sentiment Analysis Details

A core component of this project is sentiment analysis. I've integrated IMDB Watsonx.ai to categorize emotions. Watsonx.ai provides scores for five basic emotions: 'joy', 'anger', 'sadness', 'disgust', and 'fear'.

To achieve more granular and relevant verse recommendations, I've developed a set of rules based on analyzing hundreds of different inputs and their corresponding emotion scores. This allows the system to identify more nuanced emotions like 'anxiety', 'hatred', etc., resulting in more tailored verse suggestions.

To use Watsonx.ai, you'll need an IMDB account. Details on how to set this up will be available on the **Wiki** page.

## 📝 Wiki Pages

Make sure to view the following pages on the wiki for more detailed information

* [Create Angular App](url_to_create_angular_app_wiki)
* [CORS Configuration](url_to_cors_configuration_wiki)
* [Database Setup](url_to_database_setup_wiki)
* [IMDB Watsonx.ai Account Setup](url_to_imdb_watsonx_ai_account_wiki)

## 🚧 Disclaimer

This was my first experience using AWS for hosting, and my approach may not be the most optimal. I'm sharing my learning journey and the solutions I've implemented. It's not necessarily a guide to best practices, but rather a record of my exploration and experimentation. I'm committed to learning and growing, and I hope this project can be helpful to others on a similar path. 🌱

## 🤝 Contributing

Contributions are welcome! Feel free to submit pull requests with improvements or bug fixes.

## 📃 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Credits

Special thanks to everyone who has contributed to the open-source libraries used in this project.
