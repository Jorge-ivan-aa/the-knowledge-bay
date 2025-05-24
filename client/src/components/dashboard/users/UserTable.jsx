import React, { useState, useEffect } from 'react';
import { User, Star, Mail, Calendar } from 'lucide-react';
import Table from '../../common/Table';
import TableActions from '../../common/TableActions';

// Datos de ejemplo (en un caso real vendrían de una API)
const initialUsers = [
  { id: 1, username: 'maria_edu', firstName: 'María', lastName: 'García', birthDate: '1995-05-15', email: 'maria@example.com', interests: ['Matemáticas', 'Física'] },
  { id: 2, username: 'carlos_research', firstName: 'Carlos', lastName: 'López', birthDate: '1988-10-22', email: 'carlos@example.com', interests: ['Literatura', 'Historia'] },
  { id: 3, username: 'ana_science', firstName: 'Ana', lastName: 'Martínez', birthDate: '1992-03-08', email: 'ana@example.com', interests: ['Biología', 'Química'] },
  { id: 4, username: 'david_tech', firstName: 'David', lastName: 'Rodríguez', birthDate: '1990-12-30', email: 'david@example.com', interests: ['Programación', 'Inteligencia Artificial'] },
  { id: 5, username: 'sofia_arts', firstName: 'Sofía', lastName: 'Fernández', birthDate: '1993-07-18', email: 'sofia@example.com', interests: ['Arte', 'Diseño'] }
];

const UserTable = () => {
  const [users, setUsers] = useState(initialUsers);
  const [filtered, setFiltered] = useState(initialUsers);
  const [searchTerm, setSearchTerm] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [form, setForm] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  // Simulación de carga inicial
  useEffect(() => {
    setIsLoading(true);
    setTimeout(() => {
      setIsLoading(false);
    }, 1000);
  }, []);

  // Filtrar usuarios cuando cambian los criterios de búsqueda
  useEffect(() => {
    const term = searchTerm.toLowerCase();
    let result = [...users];
    
    // Filtrar por término de búsqueda
    if (term) {
      result = result.filter(user => 
        user.username.toLowerCase().includes(term) ||
        user.firstName.toLowerCase().includes(term) ||
        user.lastName.toLowerCase().includes(term) ||
        user.email.toLowerCase().includes(term)
      );
    }
    
    setFiltered(result);
  }, [searchTerm, users]);

  // Iniciar edición de un usuario
  const startEdit = (user) => {
    setEditingId(user.id);
    setForm({ ...user });
  };

  // Cancelar edición
  const cancelEdit = () => {
    setEditingId(null);
  };

  // Confirmar edición
  const confirmEdit = () => {
    const updatedUsers = users.map(user => 
      user.id === editingId ? { ...form } : user
    );
    setUsers(updatedUsers);
    setEditingId(null);
  };

  // Eliminar usuario
  const removeUser = (id) => {
    if (window.confirm('¿Está seguro que desea eliminar este usuario?')) {
      setUsers(users.filter(user => user.id !== id));
    }
  };

  // Manejar cambios en el formulario de edición
  const handleChange = (field, value) => {
    setForm({ ...form, [field]: value });
  };

  // Definición de columnas para la tabla
  const columns = [
    { key: 'user', label: 'Usuario' },
    { key: 'email', label: 'Correo' },
    { key: 'birthDate', label: 'Fecha de nacimiento' },
    { key: 'interests', label: 'Intereses' },
    { key: 'actions', label: 'Acciones', className: 'text-center' }
  ];

  // Renderizar celdas según la columna
  const renderCell = (row, column, index) => {
    const { key } = column;
    const isRowEditing = editingId === row.id;

    switch (key) {
      case 'user':
        return isRowEditing ? (
          <div className="space-y-2">
            <input
              value={form.username}
              onChange={(e) => handleChange('username', e.target.value)}
              placeholder="Nombre de usuario"
              className="w-full rounded-md border border-[var(--coastal-sea)]/30 px-2 py-1 focus:border-[var(--coastal-sea)] focus:outline-none focus:ring-1 focus:ring-[var(--coastal-sea)]"
            />
            <div className="flex gap-2">
              <input
                value={form.firstName}
                onChange={(e) => handleChange('firstName', e.target.value)}
                placeholder="Nombre"
                className="w-1/2 rounded-md border border-[var(--coastal-sea)]/30 px-2 py-1 focus:border-[var(--coastal-sea)] focus:outline-none focus:ring-1 focus:ring-[var(--coastal-sea)]"
              />
              <input
                value={form.lastName}
                onChange={(e) => handleChange('lastName', e.target.value)}
                placeholder="Apellido"
                className="w-1/2 rounded-md border border-[var(--coastal-sea)]/30 px-2 py-1 focus:border-[var(--coastal-sea)] focus:outline-none focus:ring-1 focus:ring-[var(--coastal-sea)]"
              />
            </div>
          </div>
        ) : (
          <div className="flex items-center gap-3">
            <div className="w-8 h-8 rounded-full bg-[var(--coastal-sea)]/10 flex items-center justify-center text-[var(--coastal-sea)]">
              <User size={16} />
            </div>
            <div>
              <div className="font-workSans-medium text-[var(--deep-sea)]">{row.firstName} {row.lastName}</div>
              <div className="text-xs text-[var(--open-sea)]/70">@{row.username}</div>
            </div>
          </div>
        );
      
      case 'email':
        return isRowEditing ? (
          <input
            value={form.email}
            onChange={(e) => handleChange('email', e.target.value)}
            className="w-full rounded-md border border-[var(--coastal-sea)]/30 px-2 py-1 focus:border-[var(--coastal-sea)] focus:outline-none focus:ring-1 focus:ring-[var(--coastal-sea)]"
          />
        ) : (
          <div className="flex items-center gap-2">
            <Mail size={14} className="text-[var(--coastal-sea)]" />
            {row.email}
          </div>
        );
      
      case 'birthDate':
        return isRowEditing ? (
          <input
            type="date"
            value={form.birthDate}
            onChange={(e) => handleChange('birthDate', e.target.value)}
            className="rounded-md border border-[var(--coastal-sea)]/30 px-2 py-1 focus:border-[var(--coastal-sea)] focus:outline-none focus:ring-1 focus:ring-[var(--coastal-sea)]"
          />
        ) : (
          <div className="flex items-center gap-2">
            <Calendar size={14} className="text-[var(--coastal-sea)]" />
            {row.birthDate}
          </div>
        );
      
      case 'interests':
        return isRowEditing ? (
          <input
            value={form.interests.join(', ')}
            onChange={(e) => handleChange('interests', e.target.value.split(', '))}
            className="w-full rounded-md border border-[var(--coastal-sea)]/30 px-2 py-1 focus:border-[var(--coastal-sea)] focus:outline-none focus:ring-1 focus:ring-[var(--coastal-sea)]"
          />
        ) : (
          <div className="flex flex-wrap gap-1">
            {row.interests.map((interest, i) => (
              <span key={i} className="inline-flex items-center rounded-full bg-[var(--sand)]/50 px-2 py-0.5 text-xs font-medium text-[var(--deep-sea)]">
                <Star size={10} className="mr-1 text-[var(--coastal-sea)]" />
                {interest}
              </span>
            ))}
          </div>
        );
      
      case 'actions':
        return (
          <TableActions 
            isEditing={isRowEditing}
            onEdit={() => startEdit(row)}
            onDelete={() => removeUser(row.id)}
            onConfirm={confirmEdit}
            onCancel={cancelEdit}
          />
        );
      
      default:
        return row[key];
    }
  };
  
  return (
    <div>
      <div className="mb-6">
        {/* Buscador */}
        <input
          type="text"
          placeholder="Buscar por nombre, usuario o correo..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
          className="w-full py-2 px-4 rounded-md border border-[var(--coastal-sea)]/20 focus:outline-none focus:ring-2 focus:ring-[var(--coastal-sea)]/50 focus:border-transparent"
        />
      </div>
      
      <div className="mb-4 flex justify-end">
        {/* Contador de resultados */}
        <div className="text-sm text-[var(--open-sea)]/70">
          Mostrando {filtered.length} de {users.length} usuarios
        </div>
      </div>
      
      {/* Tabla */}
      <Table
        columns={columns}
        data={filtered}
        renderCell={renderCell}
        isLoading={isLoading}
        emptyState={{
          title: "No se encontraron usuarios",
          message: "Prueba con diferentes términos de búsqueda"
        }}
      />
    </div>
  );
};

export default UserTable; 