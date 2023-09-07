import { env, pipeline } from "https://cdn.jsdelivr.net/npm/@xenova/transformers@2.5.4";

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
        init() {
            console.log("init Alpine app");
            // init transformersjs env
            //env.localModelPath="/models";
            //env.allowLocalModels = false;
            //env.remotePathTemplate = "GregaVrbancic/OTS_2023/resolve/main/{model}"
            // load model
            this.initModel();
            // load faqs
            this.getFaqs();
            this.writting = false;
        },
        async initModel() {
            console.log("init model");
            //this.answerer = await pipeline("question-answering", "minilm-uncased-squad2");
            this.answerer = await pipeline("question-answering");
            console.log("model initialized");
        },
        faqs: [],
        getFaqs() {
            fetchy("/api/faq", "GET").then((response) => {
                this.faqs = response;
            });
        },
        messages: [{text: "Hello, how can I help you?", isBot: true}],
        scroll() {
           document.getElementById("history").scrollTo(0, document.getElementById("history").scrollHeight, "smooth");
        },
        question: "",
        async askQuestion() {
            this.messages.push({text: this.question, isBot: false});

            let q = this.question;
            this.question = "";

            this.writting = true;

            const contextList = await fetchy("/api/faq", "POST", {question: q});

            if (contextList.length === 0) {
                this.messages.push({text: "Sorry, I don't know the answer to your question since I have not yet learn about this topic.", isBot: true});
                this.writting = false;
                return;
            }

            let context = "";
            for (const contextItem of contextList) {
                context += contextItem.answer + " ";
            }
            console.log(q, context);
            const answer = await this.answerer(q, context);
            console.log(answer);
            this.messages.push({text: answer.answer, isBot: true});

            this.writting = false;
        }
    }));
}
