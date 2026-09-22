const messageInput = document.getElementById("message");
const chatBox = document.getElementById("chatBox");
const sendButton = document.querySelector("button");


async function sendMessage() {

    const message = messageInput.value.trim();

    if (message === "") {
        return;
    }


    // Show user message

    chatBox.innerHTML += `
        <div class="message user">
            ${message}
        </div>
    `;


    // Clear input

    messageInput.value = "";


    // Disable button while waiting

    sendButton.disabled = true;

    chatBox.innerHTML += `
        <div class="message bot" id="loading">
            Thinking...
        </div>
    `;


    // Scroll down

    chatBox.scrollTop = chatBox.scrollHeight;


    try {

        const response = await fetch(
            "http://localhost:8080/api/chat",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify(message)
            }
        );


        if (!response.ok) {
            throw new Error("Server error");
        }


        // Your backend returns String

        const answer = await response.text();


        // Remove Thinking...

        document.getElementById("loading").remove();


        // Show AI response

        chatBox.innerHTML += `
            <div class="message bot">
                ${answer}
            </div>
        `;


    } catch (error) {

        document.getElementById("loading").remove();

        chatBox.innerHTML += `
            <div class="message bot">
                ❌ Unable to connect to server.
            </div>
        `;

        console.error(error);
    }


    // Scroll to bottom

    chatBox.scrollTop = chatBox.scrollHeight;


    sendButton.disabled = false;

    messageInput.focus();
}


// Press Enter to send

messageInput.addEventListener("keydown", function(event) {

    if (event.key === "Enter") {
        sendMessage();
    }

});