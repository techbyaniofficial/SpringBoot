// ==========================================
// CONFIGURATION
// ==========================================
const TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsInVzZXJJZCI6MSwicm9sZSI6IlVTRVIiLCJuYW1lIjoiQWxpY2UiLCJpYXQiOjE3Nzg5NzQ4MDQsImV4cCI6MTc3ODk3NTcwNH0.O7QkLvXvOb2PC6GhME2s5Od3qJ1Of4O37MqFp2haprmhnn3lBqwAVP3bxvgpkjT8BIgLqXV7bzuTzXKW0J5RpQ"; // <--- PASTE YOUR TOKEN FROM POSTMAN LOGIN RESPONSE HERE
const ORDER_URL = "http://localhost:8083/api/v1/orders";
const PRODUCT_ID = 2; // Ensure this product exists in your productdb
const INTERVAL_MS = 2000; // Place an order every 2 seconds

if (TOKEN === "YOUR_JWT_TOKEN_HERE") {
    console.error("ERROR: Please paste your valid JWT token into the script configuration!");
    process.exit(1);
}

console.log("Starting Order Load Test Demo (using native fetch)...");
console.log(`Target: ${ORDER_URL}`);
console.log(`Product ID: ${PRODUCT_ID}`);
console.log(`Interval: ${INTERVAL_MS}ms`);
console.log("------------------------------------------");

let orderCount = 0;

const placeOrder = async () => {
    orderCount++;
    const currentOrder = orderCount;
    
    try {
        const startTime = Date.now();
        const response = await fetch(ORDER_URL, {
            method: 'POST',
            headers: { 
                'Authorization': `Bearer ${TOKEN}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ productId: PRODUCT_ID })
        });

        const duration = Date.now() - startTime;
        
        if (response.ok) {
            const data = await response.json();
            console.log(`[Order #${currentOrder}] SUCCESS! ID: ${data.id} | Latency: ${duration}ms`);
        } else {
            console.error(`[Order #${currentOrder}] FAILED! Status: ${response.status}`);
        }
    } catch (error) {
        console.error(`[Order #${currentOrder}] NETWORK ERROR: ${error.message}`);
    }
};

// Start the continuous ordering loop
console.log("Press Ctrl+C to stop the demo.\n");
setInterval(placeOrder, INTERVAL_MS);
