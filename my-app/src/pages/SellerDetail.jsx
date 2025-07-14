// src/pages/SellerDetail.jsx
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

import SellerInfo from "./SellerInfo";
import ProductTable from "./ProductTable";
import CampaignTable from "./CampaignTable";
import AddCampaignForm from "./AddCampaignForm";
import UpdateCampaignForm from "./UpdateCampaignForm";

export default function SellerDetail() {
  const { id } = useParams();
  const [seller, setSeller] = useState(null);
  const [products, setProducts] = useState([]);
  const [campaigns, setCampaigns] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [balance, setBalance] = useState(null);
  const [refreshSellerInfo, setRefreshSellerInfo] = useState(0);

  // Fetch seller, products, campaigns
  useEffect(() => {
    const fetchAll = async () => {
      try {
        const [sellerRes, productRes, campaignRes] = await Promise.all([
          fetch(`http://localhost:8080/api/v1/seller/${id}`),
          fetch(`http://localhost:8080/api/v1/product?sellerId=${id}`),
          fetch(`http://localhost:8080/api/v1/campaign/${id}`)
        ]);

        if (!sellerRes.ok) throw new Error("Failed to load seller.");
        if (!productRes.ok) throw new Error("Failed to load products.");
        if (!campaignRes.ok) throw new Error("Failed to load campaigns.");

        const sellerData = await sellerRes.json();
        const productData = await productRes.json();
        const campaignData = await campaignRes.json();

        setSeller(sellerData);
        setProducts(productData);
        setCampaigns(campaignData);
        setSeller(sellerData);
        setBalance(sellerData.emeraldBalance); // 👈 add this

        console.log(campaignData);
        console.log(products)
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchAll();
  }, [id]);

const handleNewCampaign = (newCampaign) => {
  setCampaigns(prev => [...prev, newCampaign]);
  setBalance((prev) => prev - Number(newCampaign.campaignFund)); // 👈 instant update
};
  const handleDeleteCampaign = (deletedId) => {
    setCampaigns((prev) => prev.filter((c) => c.id !== deletedId));
  };

  const handleUpdateProduct = (updatedProduct) => {
    setProducts((prev) =>
      prev.map((p) => (p.id === updatedProduct.id ? updatedProduct : p))
    );
  };

    const handleUpdateCampaign = (updatedCampaign) => {
      setCampaigns((prev) =>
        prev.map((c) => (c.id === updatedCampaign.id ? updatedCampaign : c))
      );
       setRefreshSellerInfo((count) => count + 1);
    };



  if (loading) return <div className="p-6">Loading seller details...</div>;
  if (error) return <div className="p-6 text-red-600">Error: {error}</div>;

  return (
    <div className="flex flex-col gap-8 p-6">
      <SellerInfo seller={seller}  balance={balance} setBalance={setBalance} refresh={refreshSellerInfo}/>
      <ProductTable products={products} />
      <div className="flex flex-col lg:flex-row gap-6">
          {console.log(products)}
        <AddCampaignForm sellerId={id} products={products} onNewCampaign={handleNewCampaign} />
        <UpdateCampaignForm campaigns={campaigns} products={products} onUpdateCampaign={handleUpdateCampaign}  />
      </div>
      <CampaignTable campaigns={campaigns} onDelete={handleDeleteCampaign} onUpdate={handleUpdateProduct} />
    </div>
  );
}
