
async function sendMessage() {

    const messageInput = document.getElementById("message");
    const responseBox = document.getElementById("response");

    const message = messageInput.value.trim();

    if (!message) {
        return;
    }

    responseBox.innerText = "Loading...";

    try {

        const response = await fetch("http://localhost:8080/api/chat", {
            method: "POST",

            headers: {
                "Content-Type": "application/json"
            },

            // Send String because @RequestBody String message
            body: JSON.stringify(message)
        });

        if (!response.ok) {
            throw new Error("Server error: " + response.status);
        }

        // Backend returns String, not JSON
        const data = await response.text();

        responseBox.innerText = data;

    } catch (error) {

        console.error(error);

        responseBox.innerText =
            "Error connecting to backend.\n" +
            error.message;
    }
}

