import { useState } from "react";

export default function CampaignTable({ campaigns, onDelete }) {
  const [deletingId, setDeletingId] = useState(null);
  const API_URL ='https://adcampain.onrender.com';

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this campaign?")) return;

    setDeletingId(id);
    try {
      const res = await fetch(`${API_URL}/api/v1/campaign/${id}`, {
        method: "DELETE",
      });
      if (!res.ok) throw new Error("Failed to delete campaign");
      onDelete(id); // Notify parent to remove from state
    } catch (err) {
      alert(err.message || "Error deleting campaign");
    } finally {
      setDeletingId(null);
    }
  };

  return (
    <div className="bg-white rounded shadow p-4 overflow-x-auto">
      <h2 className="text-2xl font-semibold mb-4">Campaigns</h2>
      {(!campaigns || campaigns.length === 0) ? (
        <p>No campaigns found.</p>
      ) : (
        <table className="table-auto w-full text-left border-collapse border border-gray-300">
          <thead>
            <tr className="bg-gray-100">
              <th className="border border-gray-300 px-3 py-1">ID</th>
              <th className="border border-gray-300 px-3 py-1">Name</th>
              <th className="border border-gray-300 px-3 py-1">Bid Amount</th>
              <th className="border border-gray-300 px-3 py-1">Campaign Fund</th>
              <th className="border border-gray-300 px-3 py-1">Status</th>
              <th className="border border-gray-300 px-3 py-1">Town</th>
              <th className="border border-gray-300 px-3 py-1">Keywords</th>
              <th className="border border-gray-300 px-3 py-1">Radius (km)</th>
              <th className="border border-gray-300 px-3 py-1">Product</th>
              <th className="border border-gray-300 px-3 py-1">Actions</th>
            </tr>
          </thead>
          <tbody>
            {campaigns.map((c) => (
              <tr key={c.id} className="hover:bg-gray-50">
                <td className="border border-gray-300 px-3 py-1">{c.id}</td>
                <td className="border border-gray-300 px-3 py-1">{c.name}</td>
                <td className="border border-gray-300 px-3 py-1">{c.bidAmount}</td>
                <td className="border border-gray-300 px-3 py-1">{c.campaignFund}</td>
                <td className="border border-gray-300 px-3 py-1">{c.status}</td>
                <td className="border border-gray-300 px-3 py-1">{c.town}</td>
                <td className="border border-gray-300 px-3 py-1">
                  {c.keywords && c.keywords.length > 0 ? c.keywords.join(", ") : "-"}
                </td>
                <td className="border border-gray-300 px-3 py-1">{c.radius ?? "-"}</td>
                <td className="border border-gray-300 px-3 py-1">
                  {c.productDTO?.name ?? "-"}
                </td>
                <td className="border border-gray-300 px-3 py-1">
                  <button
                    className="bg-red-600 text-white px-3 py-1 rounded disabled:opacity-50"
                    disabled={deletingId === c.id}
                    onClick={() => handleDelete(c.id)}
                  >
                    {deletingId === c.id ? "Deleting..." : "Delete"}
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
