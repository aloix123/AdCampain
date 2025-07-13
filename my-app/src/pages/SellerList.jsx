// pages/SellerList.jsx
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

export default function SellerList() {
  const [sellers, setSellers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetch("http://localhost:8080/api/v1/seller")
      .then((res) => {
        if (!res.ok) throw new Error("Failed to fetch sellers");
        return res.json();
      })
      .then((data) => {
        setSellers(data);
        setLoading(false);
      })
      .catch((err) => {
        setError(err.message);
        setLoading(false);
      });
  }, []);

  if (loading) return <div className="p-6">Loading sellers...</div>;
  if (error) return <div className="p-6 text-red-600">Error: {error}</div>;

  return (
    <div className="max-h-screen flex flex-col items-center justify-center bg-gray-100 p-6">
      <div className="text-center mb-8 max-w-xl">
        <h1 className="text-3xl font-bold mb-2">
          Welcome to my AdCampaign website!!!!!
        </h1>
        <h2 className="text-xl">Please choose your seller</h2>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 justify-items-center w-full max-w-5xl">
        {sellers.map(({ id, name, emeraldBalance }) => (
          <div
            key={id}
            className="bg-white p-6 rounded shadow w-64 text-center"
          >
            <Link
              to={`/seller/${id}`}
              className="text-xl font-semibold mb-2 text-blue-600 hover:underline block"
            >
              {name}
            </Link>
            <p className="text-gray-700">
              Emerald Balance:{" "}
              <span className="font-mono">{emeraldBalance.toFixed(2)}</span>
            </p>
          </div>
        ))}
      </div>
    </div>
  );
}
