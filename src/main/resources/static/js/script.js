import { env, pipeline } from 'https://cdn.jsdelivr.net/npm/@xenova/transformers@2.5.4';

const bodyTag = document.querySelector("html");

let fetchy = async (url, method = "GET", data = {}) => {
    if (method === "GET") {
        data = undefined;
    } else {
        data = JSON.stringify(data);
    }

    let response = await fetch(url, {
        method: method,
        headers: {
            "Content-Type": "application/json",
        },
        body: data
    });

    if (response.redirected) {
        window.location.replace(response.url);
    }

    const contentType = response.headers.get("content-type");

    if(contentType && contentType.indexOf("json") !== -1) {
        return response.json();
    } else {
        return response.text();
    }
};

export default function (Alpine) {
    Alpine.data("initApp", () => ({
        async init() {
            console.log("init Alpine app");
            // load model
            this.answerer = await pipeline('question-answering');
            this.getFaqs();
        },
        faqs: [],
        getFaqs() {
            fetchy("/api/faq", "GET").then((response) => {
                this.faqs = response;
            });
        },
        messages: [{text: "Hello, how can I help you?", isBot: true}],
        question: "",
        async askQuestion() {
            console.log(this.question);
            this.messages.push({text: this.question, isBot: false});
            const contextList = await fetchy("/api/faq", "POST", {question: this.question});
            let context = "";
            for (const contextItem of contextList) {
                context += contextItem.answer + " ";
            }
            
            const answer = await this.answerer(this.question, context);
            console.log(answer);
            this.messages.push({text: answer.answer, isBot: true});
            this.question = "";
            //console.log(answer);
        }
    }));
}
