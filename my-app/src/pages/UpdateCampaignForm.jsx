import { useEffect, useState } from "react";

export default function UpdateCampaignForm({ campaigns, products, onUpdateCampaign }) {
  const [selectedId, setSelectedId] = useState("");
  const [form, setForm] = useState(null);
  const [typedKeyword, setTypedKeyword] = useState("");
  const [updatedData, setUpdatedData] = useState(null);
  const [keywordsList, setKeywordsList] = useState([]);
  const [townsList, setTownsList] = useState([]);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState(null);

  // Fetch keywords & towns
  useEffect(() => {
    fetch("http://localhost:8080/api/v1/campaign/keyword")
      .then((res) => res.text())
      .then((text) => text ? JSON.parse(text) : [])
      .then(setKeywordsList)
      .catch(console.error);

    fetch("http://localhost:8080/api/v1/campaign/town")
      .then((res) => res.text())
      .then((text) => text ? JSON.parse(text) : [])
      .then(setTownsList)
      .catch(console.error);
  }, []);

  // Fill form when campaign selected
  useEffect(() => {
    const campaign = campaigns.find((c) => c.id === parseInt(selectedId));
    if (campaign) {
      setForm({
        id: campaign.id,
        name: campaign.name,
        bidAmount: campaign.bidAmount,
        campaignFund: campaign.campaignFund,
        status: campaign.status,
        town: campaign.town,
        radius: campaign.radius,
        keywords: campaign.keywords || [],
        productDTO: products.find((p) => p.name === campaign.productName) || null,
      });
      setUpdatedData(null); // clear previous update preview
      setError(null);
    }
  }, [selectedId, campaigns, products]);

  const addKeyword = (kw) => {
    if (kw && !form.keywords.includes(kw)) {
      setForm((f) => ({ ...f, keywords: [...f.keywords, kw] }));
    }
    setTypedKeyword("");
  };

  const removeKeyword = (kw) => {
    setForm((f) => ({ ...f, keywords: f.keywords.filter((k) => k !== kw) }));
  };

 const filteredKeywords = form
   ? keywordsList.filter(
       (kw) => kw.includes(typedKeyword) && !form.keywords.includes(kw)
     )
   : [];


 const handleSubmit = async (e) => {
   e.preventDefault();
   setSubmitting(true);
   setError(null);

   try {
     const res = await fetch("http://localhost:8080/api/v1/campaign", {
       method: "PUT",
       headers: { "Content-Type": "application/json" },
       body: JSON.stringify(form),
     });

     const text = await res.text();
     const data = text ? JSON.parse(text) : null;

     if (!res.ok) {
       // Parse message object if needed
       let errorMessage = "";
       if (data?.message && typeof data.message === "object") {
         errorMessage = Object.entries(data.message)
           .map(([field, msg]) => `${field}: ${msg}`)
           .join(", ");
       } else {
         errorMessage = data?.message || "Update failed.";
       }
       throw new Error(errorMessage);
     }

     setUpdatedData(data);
     onUpdateCampaign(data);
     alert("Campaign updated!");
   } catch (err) {
     setError(err.message);
   } finally {
     setSubmitting(false);
   }
 };


  return (
    <div className="bg-white p-4 rounded shadow mt-6">
      <h2 className="text-xl font-semibold mb-4">Update Campaign</h2>

      <div className="mb-3">
        <label className="block mb-1">Select Campaign</label>
        <select
          className="border px-2 py-1 w-full"
          value={selectedId}
          onChange={(e) => setSelectedId(e.target.value)}
        >
          <option value="">-- Select Campaign --</option>
          {campaigns.map((c) => (
            <option key={c.id} value={c.id}>
              {c.id} - {c.name}
            </option>
          ))}
        </select>
      </div>

      {form && (
        <form onSubmit={handleSubmit} className="bg-white rounded shadow p-4">
          <div className="mb-2">
            <label>Name *</label>
            <input
              type="text"
              value={form.name}
              onChange={(e) => setForm({ ...form, name: e.target.value })}
              className="w-full border px-2 py-1"
              required
            />
          </div>

          <div className="mb-2">
            <label>Bid Amount *</label>
            <input
              type="number"
              step="0.01"
              value={form.bidAmount}
              onChange={(e) => setForm({ ...form, bidAmount: e.target.value })}
              className="w-full border px-2 py-1"
              required
            />
          </div>

          <div className="mb-2">
            <label>Campaign Fund *</label>
            <input
              type="number"
              step="0.01"
              value={form.campaignFund}
              onChange={(e) => setForm({ ...form, campaignFund: e.target.value })}
              className="w-full border px-2 py-1"
              required
            />
          </div>

          <div className="mb-2">
            <label>Status</label>
            <select
              value={form.status}
              onChange={(e) => setForm({ ...form, status: e.target.value })}
              className="w-full border px-2 py-1"
            >
              <option value="ON">ON</option>
              <option value="OFF">OFF</option>
            </select>
          </div>

          <div className="mb-2">
            <label>Town</label>
            <select
              value={form.town}
              onChange={(e) => setForm({ ...form, town: e.target.value })}
              className="w-full border px-2 py-1"
            >
              <option value="">Select a town</option>
              {townsList.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </div>

          <div className="mb-2">
            <label>Radius (km)</label>
            <input
              type="number"
              value={form.radius}
              onChange={(e) => setForm({ ...form, radius: e.target.value })}
              className="w-full border px-2 py-1"
              min={0}
            />
          </div>

          <div className="mb-2">
            <label>Keywords</label>
            <div className="flex flex-wrap gap-1 mb-1">
              {form.keywords.map((kw) => (
                <span
                  key={kw}
                  className="bg-gray-200 rounded px-2 py-0.5 flex items-center gap-1"
                >
                  {kw}
                  <button type="button" onClick={() => removeKeyword(kw)}>
                    ×
                  </button>
                </span>
              ))}
            </div>
            <input
              type="text"
              value={typedKeyword}
              onChange={(e) => setTypedKeyword(e.target.value)}
              className="w-full border px-2 py-1"
              placeholder="Type to search keywords..."
            />
            {typedKeyword && filteredKeywords.length > 0 && (
              <ul className="border bg-white max-h-32 overflow-y-auto mt-1">
                {filteredKeywords.map((kw) => (
                  <li
                    key={kw}
                    className="px-2 py-1 hover:bg-gray-100 cursor-pointer"
                    onClick={() => addKeyword(kw)}
                  >
                    {kw}
                  </li>
                ))}
              </ul>
            )}
          </div>

          <div className="mb-2">
            <label>Product *</label>
            <select
              value={form.productDTO?.id || ""}
              onChange={(e) => {
                const selectedProduct = products.find(
                  (p) => p.id === Number(e.target.value)
                );
                if (selectedProduct) {
                  setForm((f) => ({
                    ...f,
                    productDTO: {
                      id: selectedProduct.id,
                      name: selectedProduct.name,
                      sellerId: selectedProduct.sellerId,
                    },
                  }));
                } else {
                  setForm((f) => ({ ...f, productDTO: null }));
                }
              }}
              className="w-full border px-2 py-1"
              required
            >
              <option value="">Select a product</option>
              {products.map((p) => (
                <option key={p.id} value={p.id}>
                  {p.name}
                </option>
              ))}
            </select>
          </div>

          {error && <div className="text-red-600 mb-2">{error}</div>}

          <button
            type="submit"
            className="bg-blue-600 text-white px-4 py-2 rounded"
            disabled={submitting}
          >
            {submitting ? "Updating..." : "Update Campaign"}
          </button>
        </form>
      )}

      {updatedData && (
        <div className="mt-4 bg-green-100 text-green-800 p-3 rounded">
          <h4 className="font-bold mb-1">Updated Campaign:</h4>
          <pre className="text-sm whitespace-pre-wrap">
            {JSON.stringify(updatedData, null, 2)}
          </pre>
        </div>
      )}
    </div>
  );
}
