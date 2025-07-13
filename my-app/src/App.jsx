// App.jsx
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import SellerDetail from "./pages/SellerDetail";
import SellerList from "./pages/SellerList";

export default function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<SellerList />} />
        <Route path="/seller/:id" element={<SellerDetail />} />
      </Routes>
    </Router>
  );
}
