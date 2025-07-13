export default function ProductTable({ products }) {
  return (
    <div className="bg-white rounded shadow p-4 overflow-x-auto">
      <h2 className="text-2xl font-semibold mb-4">Products you have</h2>
      {products.length === 0 ? (
        <p>No products found.</p>
      ) : (
        <table className="table-auto w-full text-left">
          <thead className="bg-gray-200">
            <tr>
              <th className="px-4 py-2 border">ID</th>
              <th className="px-4 py-2 border">Name</th>
            </tr>
          </thead>
          <tbody>
            {products.map((product) => (
              <tr key={product.id} className="hover:bg-gray-50">
                <td className="px-4 py-2 border">{product.id}</td>
                <td className="px-4 py-2 border">{product.name}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
