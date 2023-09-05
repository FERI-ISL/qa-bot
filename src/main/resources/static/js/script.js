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
            // TODO: implementirajte inicializacijo aplikacije.
            this.writting = false;
        },
        async initModel() {
            // TODO: implementirajte inicializacijo LLM modela.
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
            // TODO: implementirajte funckionalnost za pridobivanje in prikaz odgovora na zastavljeno vprasanje.
        }
    }));
}
