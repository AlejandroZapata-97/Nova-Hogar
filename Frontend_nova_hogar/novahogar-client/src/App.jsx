import React, { useState, useEffect } from 'react';
import axios from 'axios';

// --- ESTILOS ---
const API_URL = "http://localhost:8080/api";

const btnStyle = (active) => ({
  padding: '12px 24px', backgroundColor: active ? '#1877f2' : '#fff',
  color: active ? '#fff' : '#000', border: 'none', borderRadius: '8px', cursor: 'pointer', boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
});
const gridStyle = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))', gap: '20px' };
const cardStyle = (low) => ({ background: '#fff', borderRadius: '12px', overflow: 'hidden', boxShadow: '0 4px 6px rgba(0,0,0,0.1)', border: low ? '2px solid #ff4d4d' : 'none' });
const imgStyle = { width: '100%', height: '160px', objectFit: 'cover' };
const calcContainer = { maxWidth: '500px', margin: '0 auto', background: '#fff', padding: '30px', borderRadius: '15px' };
const inputStyle = { padding: '12px', borderRadius: '8px', border: '1px solid #ddd', width: '90%', marginBottom: '10px' };
const actionBtn = { padding: '12px', background: '#1877f2', color: '#fff', border: 'none', borderRadius: '8px', cursor: 'pointer', width: '100%' };
const resultStyle = { marginTop: '20px', padding: '15px', background: '#e7f3ff', borderRadius: '8px', textAlign: 'center' };
const sellBtn = { width: '100%', padding: '10px', background: '#2ecc71', color: '#fff', border: 'none', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' };

// --- APLICACIÓN ---
function App() {
  const [productos, setProductos] = useState([]);
  const [tab, setTab] = useState('inventario');
  const [errorApp, setErrorApp] = useState("");
  
  const [calc, setCalc] = useState({ ancho: '', largo: '', idProd: '' });
  const [resultado, setResultado] = useState(null);

  const fetchData = async () => {
    try {
      const res = await axios.get(`${API_URL}/productos`);
      setProductos(res.data);
      setErrorApp("");
    } catch (err) {
      setErrorApp("No se pudo conectar al Backend. Revisa Spring Boot.");
    }
  };

  useEffect(() => {
    fetchData();
    const timer = setInterval(fetchData, 5000);
    return () => clearInterval(timer);
  }, []);

  const procesarCalculo = () => {
    if (!calc.idProd) { alert("Selecciona un producto"); return; }
    const p = productos.find(x => x.idProducto === parseInt(calc.idProd));
    if (!p || !calc.ancho || !calc.largo) return;

    const m2 = calc.ancho * calc.largo;
    const factor = p.nombreProducto.toLowerCase().includes('pvc') ? 1.19 : 3.0;
    const total = Math.ceil(m2 / factor);

    setResultado({ m2: m2.toFixed(2), total, nombre: p.nombreProducto });
  };

  const registrarVenta = async () => {
    try {
      await axios.post(`${API_URL}/inventario/movimiento`, {
        tipoMovimiento: 'Salida',
        cantidad: resultado.total,
        motivo: 'Venta calculada',
        producto: { idProducto: parseInt(calc.idProd) },
        usuario: { idUsuario: 1 }
      });
      alert("¡Venta registrada exitosamente!");
      setResultado(null);
      fetchData();
    } catch (err) { alert("Error al registrar la venta"); }
  };

  return (
    <div style={{ fontFamily: 'sans-serif', padding: '20px', backgroundColor: '#f0f2f5', minHeight: '100vh' }}>
      
      {errorApp && <div style={{background: '#ff7675', color: 'white', padding: '10px', textAlign: 'center', marginBottom: '20px'}}>{errorApp}</div>}

      <nav style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginBottom: '30px' }}>
        <button onClick={() => setTab('inventario')} style={btnStyle(tab === 'inventario')}>📦 Inventario</button>
        <button onClick={() => setTab('calculadora')} style={btnStyle(tab === 'calculadora')}>🧮 Calculadora</button>
      </nav>

      {tab === 'inventario' ? (
        <div style={gridStyle}>
          {productos.length === 0 && !errorApp ? <p>Cargando productos...</p> : null}
          {productos.map(p => (
            <div key={p.idProducto} style={cardStyle(p.stockActual <= p.stockMinimo)}>
              <img src={p.urlImagen || 'https://via.placeholder.com/150'} style={imgStyle} alt="prod" />
              <div style={{padding: '15px'}}>
                <h3 style={{margin: '0'}}>{p.nombreProducto}</h3>
                <p style={{color: '#27ae60', fontWeight: 'bold'}}>${p.precioVenta.toLocaleString()}</p>
                <p>Stock: <strong>{p.stockActual}</strong></p>
                {p.stockActual <= p.stockMinimo && <small style={{color: 'red'}}>⚠️ REABASTECER</small>}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div style={calcContainer}>
          <h2>Cálculo de Techos y Cielos</h2>
          <div>
            <select onChange={e => setCalc({...calc, idProd: e.target.value})} style={inputStyle}>
              <option value="">Seleccione Material...</option>
              {productos.map(p => <option key={p.idProducto} value={p.idProducto}>{p.nombreProducto}</option>)}
            </select>
            <input type="number" placeholder="Ancho (m)" onChange={e => setCalc({...calc, ancho: e.target.value})} style={inputStyle} />
            <input type="number" placeholder="Largo (m)" onChange={e => setCalc({...calc, largo: e.target.value})} style={inputStyle} />
            <button onClick={procesarCalculo} style={actionBtn}>Calcular</button>
          </div>

          {resultado && (
            <div style={resultStyle}>
              <p>Área: {resultado.m2} m²</p>
              <h4>Necesitas: {resultado.total} unidades</h4>
              <button onClick={registrarVenta} style={sellBtn}>Efectuar Venta ({resultado.total} uds)</button>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default App;