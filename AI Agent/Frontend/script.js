const messageInput = document.getElementById("message");
const chatBox = document.getElementById("chatBox");
const sendButton = document.getElementById("sendButton");
const preview = document.getElementById("websitePreview");
const previewStatus = document.getElementById("previewStatus");

let currentProject = null;


// ================= SEND MESSAGE =================

async function sendMessage() {

    const message = messageInput.value.trim();

    if (message === "") {
        return;
    }

    // Show user message
    addMessage(message, "user");

    messageInput.value = "";

    sendButton.disabled = true;

    // Loading message
    const loading = document.createElement("div");

    loading.className = "message bot";
    loading.id = "loading";

    loading.innerHTML = `
        <div class="message-title">
            ✦ WebCraft Agent
        </div>

        Building your website...
    `;

    chatBox.appendChild(loading);

    scrollChat();


    try {

        const response = await fetch(
            "http://localhost:8080/website",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(message)
            }
        );


        // Get response even when HTTP status is an error
        const responseText = await response.text();

        console.log("HTTP STATUS:", response.status);
        console.log("BACKEND RESPONSE:", responseText);


        // Remove loading
        const loadingMessage =
            document.getElementById("loading");

        if (loadingMessage) {
            loadingMessage.remove();
        }


        // Backend error
        if (!response.ok) {

            throw new Error(
                `HTTP ${response.status}: ${responseText}`
            );
        }


        // =========================
        // BACKEND RESPONSE
        // =========================

        const answer = responseText;


        // Show AI response
        addMessage(answer, "bot");


        /*
         * IMPORTANT:
         *
         * We no longer extract HTML from the
         * AI response.
         *
         * The AI creates the files using
         * WebsiteTools.
         */


        /*
         * If backend returns JSON such as:
         *
         * {
         *   "message": "Website created",
         *   "project": "portfolio"
         * }
         *
         * we can load that project.
         */


        try {

            const data = JSON.parse(answer);

            if (data.project) {

                currentProject = data.project;

                loadWebsitePreview(data.project);

            }

        } catch (e) {

            /*
             * Backend currently returns plain String.
             * That's okay for now.
             *
             * We don't try to extract HTML anymore.
             */

            console.log(
                "Backend returned plain text."
            );
        }


    } catch (error) {

        const loadingMessage =
            document.getElementById("loading");

        if (loadingMessage) {
            loadingMessage.remove();
        }


        addMessage(
            "❌ " + error.message,
            "bot"
        );


        console.error(
            "FULL BACKEND ERROR:",
            error
        );

    }


    sendButton.disabled = false;

    messageInput.focus();

    scrollChat();
}


// ================= ADD MESSAGE =================

function addMessage(text, type) {

    const message = document.createElement("div");

    message.className = `message ${type}`;


    if (type === "bot") {

        message.innerHTML = `
            <div class="message-title">
                ✦ WebCraft Agent
            </div>

            ${formatResponse(text)}
        `;

    } else {

        message.textContent = text;

    }


    chatBox.appendChild(message);

    scrollChat();
}


// ================= FORMAT RESPONSE =================

function formatResponse(text) {

    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/\n/g, "<br>");
}


// ================= SUGGESTIONS =================

function useSuggestion(text) {

    messageInput.value = text;

    messageInput.focus();
}


// ================= SCROLL =================

function scrollChat() {

    chatBox.scrollTop =
        chatBox.scrollHeight;
}


// ================= LOAD WEBSITE =================

function loadWebsitePreview(project) {

    console.log(
        "Loading project:",
        project
    );


    /*
     * Backend endpoint that we will create:
     *
     * GET /website/preview/{project}
     */

    const previewUrl =
        `http://localhost:8080/website/preview/${encodeURIComponent(project)}`;


    preview.src =
        previewUrl;


    previewStatus.textContent =
        "Website generated successfully";
}


// ================= REFRESH =================

function refreshPreview() {

    if (!currentProject) {
        return;
    }


    loadWebsitePreview(currentProject);
}


// ================= OPEN PREVIEW =================

function openPreview() {

    if (!currentProject) {
        return;
    }


    const previewUrl =
        `http://localhost:8080/website/preview/${encodeURIComponent(currentProject)}`;


    window.open(
        previewUrl,
        "_blank"
    );
}


// ================= NEW PROJECT =================

function newProject() {

    currentProject = null;

    chatBox.innerHTML = `
        <div class="message bot">

            <div class="message-title">
                ✦ WebCraft Agent
            </div>

            New project created.

            <br><br>

            What website would you like me to build?

        </div>
    `;


    preview.src = "";

    previewStatus.textContent =
        "Waiting for instructions";
}


// ================= ENTER KEY =================

messageInput.addEventListener(
    "keydown",
    function(event) {

        if (
            event.key === "Enter" &&
            !event.shiftKey
        ) {

            event.preventDefault();

            sendMessage();
        }
    }
);