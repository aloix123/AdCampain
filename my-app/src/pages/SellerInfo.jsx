import { useState, useEffect } from "react";
import { Link } from "react-router-dom";

export default function SellerInfo({ seller, balance, setBalance, refresh }) {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [localSeller, setLocalSeller] = useState(seller);
  const API_URL ='https://adcampain.onrender.com';

  // Refetch seller data when 'refresh' changes
  useEffect(() => {
    const fetchSeller = async () => {
      try {
        const res = await fetch(`${API_URL}/api/v1/seller/${seller.id}`);
        if (!res.ok) throw new Error("Failed to fetch seller info.");
        const data = await res.json();
        setLocalSeller(data);
        setBalance(data.emeraldBalance);
      } catch (err) {
        console.error(err);
      }
    };

    if (refresh !== undefined) {
      fetchSeller();
    }
  }, [refresh, seller.id, setBalance]);

  const handleAddMoney = async () => {
    setLoading(true);
    setError(null);

    try {
      const res = await fetch(
        `${API_URL}/api/v1/seller/money/${seller.id}`,  // <-- corrected backticks here
        { method: "PUT" }
      );

      if (!res.ok) throw new Error("Failed to update balance");

      const newBalance = await res.json(); // expecting BigDecimal (number)
      setBalance(newBalance);
    } catch (err) {
      setError(err.message || "Unexpected error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-white rounded shadow p-6 text-center">
      <h1 className="text-3xl font-bold mb-4">Hi {localSeller?.name}!</h1>

      <p className="text-xl mb-4">
        Your balance is{" "}
        <span className="font-mono">{Number(balance).toFixed(2)}</span>
      </p>

      <button
        onClick={handleAddMoney}
        className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700 mb-4"
        disabled={loading}
      >
        {loading ? "Adding..." : "Add Money"}
      </button>

      {error && <p className="text-red-600 mb-2">{error}</p>}

      <br />
      <Link to="/" className="text-blue-600 hover:underline">
        ← Back to seller list
      </Link>
    </div>
  );
}
