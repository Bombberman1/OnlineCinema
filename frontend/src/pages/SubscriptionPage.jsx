import { useEffect, useState } from "react";
import { getSubscription, createSubscription } from "../api/subscription";
import { useToast } from "../components/ToastProvider";
import "../styles/subscription.css";

const SubscriptionPage = () => {
    const { showToast } = useToast();
    const [subscription, setSubscription] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const load = async () => {
            try {
                const data = await getSubscription();
                setSubscription(data);
            } catch {
                setSubscription(null);
            } finally {
                setLoading(false);
            }
        };

        load();
    }, []);

    const handleBuy = async (days, price) => {
        try {
            await createSubscription({
                days: days,
                price: price,
            });

            const updated = await getSubscription();
            setSubscription(updated);

            showToast("Subscription activated", "success");
        } catch (err) {
            showToast(
                err.response?.data?.message || "Failed to subscribe",
                "error"
            );
        }
    };

    if (loading) {
        return <div className="subscription-page">Loading...</div>;
    }

    return (
        <div className="subscription-page">
            <h2>Subscription</h2>

            {subscription ? (
                <div className="subscription-active">
                    <p>
                        <strong>Active until:</strong>{" "}
                        {subscription.endDate}
                    </p>
                    <p>
                        <strong>Price:</strong>{" "}
                        {subscription.price}$
                    </p>
                </div>
            ) : (
                <div className="subscription-free">
                    <p>You are using free plan (with ads)</p>

                    <div className="subscription-options">
                        <button
                            onClick={() => handleBuy(30, 9.99)}
                        >
                            Buy 30 days — $9.99
                        </button>

                        <button
                            onClick={() => handleBuy(90, 24.99)}
                        >
                            Buy 90 days — $24.99
                        </button>
                    </div>
                </div>
            )}
        </div>
    );
};

export default SubscriptionPage;
