import { useEffect, useState } from "react";

export default function AddCampaignForm({ sellerId, onNewCampaign, products }) {
  const API_URL ='https://adcampain.onrender.com';
  const [keywordsList, setKeywordsList] = useState([]);
  const [townsList, setTownsList] = useState([]);
  const [form, setForm] = useState({
    name: "",
    bidAmount: "",
    campaignFund: "",
    status: "ON",
    town: "",
    keywords: [],
    radius: "",
    productDTO: null,
  });
  const [typedKeyword, setTypedKeyword] = useState("");
  const [errors, setErrors] = useState(null);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    const fetchKeywords = async () => {
      try {
        const res = await fetch(`${API_URL}/api/v1/campaign/keyword`);
        const text = await res.text();
        if (!res.ok) {
          const error = text ? JSON.parse(text) : { message: "Failed to load keywords" };
          throw new Error(error.message || "Failed to load keywords");
        }
        setKeywordsList(text ? JSON.parse(text) : []);
      } catch (error) {
        console.error("Error fetching keywords:", error);
        setErrors(`Error loading keywords: ${error.message}`);
      }
    };

    const fetchTowns = async () => {
      try {
        const res = await fetch(`${API_URL}/api/v1/campaign/town`);
        const text = await res.text();
        if (!res.ok) {
          const error = text ? JSON.parse(text) : { message: "Failed to load towns" };
          throw new Error(error.message || "Failed to load towns");
        }
        setTownsList(text ? JSON.parse(text) : []);
      } catch (error) {
        console.error("Error fetching towns:", error);
        setErrors(`Error loading towns: ${error.message}`);
      }
    };

    fetchKeywords();
    fetchTowns();
  }, []);

  const addKeyword = (kw) => {
    if (kw && !form.keywords.includes(kw)) {
      setForm((f) => ({ ...f, keywords: [...f.keywords, kw] }));
    }
    setTypedKeyword("");
  };

  const removeKeyword = (kw) => {
    setForm((f) => ({ ...f, keywords: f.keywords.filter((k) => k !== kw) }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors(null);
    setSubmitting(true);

    try {
      const res = await fetch(`${API_URL}/api/v1/campaign`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const text = await res.text();

      if (!res.ok) {
        const errorResponse = text ? JSON.parse(text) : { message: "Unknown error" };
        let errorMessage = "";

        if (typeof errorResponse.message === "object" && errorResponse.message !== null) {
          errorMessage = Object.entries(errorResponse.message)
            .map(([field, msg]) => `${field}: ${msg}`)
            .join(", ");
        } else {
          errorMessage = errorResponse.message || "Unknown error";
        }

        throw new Error(errorMessage);
      }

      const newCampaign = text ? JSON.parse(text) : {};
      onNewCampaign(newCampaign);

      setForm({
        name: "",
        bidAmount: "3",
        campaignFund: "",
        status: "ON",
        town: "",
        keywords: [],
        radius: "",
        productDTO: null,
      });
    } catch (err) {
      setErrors(err.message || "Unexpected error");
    } finally {
      setSubmitting(false);
    }
  };

  const filteredKeywords = keywordsList.filter(
    (kw) => kw.includes(typedKeyword) && !form.keywords.includes(kw)
  );

  return (
    <form onSubmit={handleSubmit} className="bg-white rounded shadow p-4">
      <h3 className="text-xl font-semibold mb-3">Add Campaign</h3>

      <div className="mb-2">
        <label>Name *</label>
        <input
          type="text"
          value={form.name}
          onChange={(e) => setForm((f) => ({ ...f, name: e.target.value }))}
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
          onChange={(e) => setForm((f) => ({ ...f, bidAmount: e.target.value }))}
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
          onChange={(e) => setForm((f) => ({ ...f, campaignFund: e.target.value }))}
          className="w-full border px-2 py-1"
          required
        />
      </div>

      <div className="mb-2">
        <label>Status</label>
        <select
          value={form.status}
          onChange={(e) => setForm((f) => ({ ...f, status: e.target.value }))}
          className="w-full border px-2 py-1"
        >
          <option>ON</option>
          <option>OFF</option>
        </select>
      </div>

      <div className="mb-2">
        <label>Town</label>
        <select
          value={form.town}
          onChange={(e) => setForm((f) => ({ ...f, town: e.target.value }))}
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
          onChange={(e) => setForm((f) => ({ ...f, radius: e.target.value }))}
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
          value={form.productDTO ? form.productDTO.id : ""}
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
                  sellerId: sellerId,
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

      {errors && <div className="text-red-600 mb-2">{errors}</div>}

      <button
        type="submit"
        className="bg-blue-600 text-white px-4 py-2 rounded"
        disabled={submitting}
      >
        {submitting ? "Creating..." : "Add Campaign"}
      </button>
    </form>
  );
}
