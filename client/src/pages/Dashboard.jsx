import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import NavigationBar from '../components/layout/NavigationBar';

// Componentes para los widgets del dashboard
const DashboardWidget = ({ title, icon, description, linkTo, color }) => {
  return (
    <Link 
      to={linkTo} 
      className={`block p-6 rounded-lg shadow-md transition-all duration-300 hover:shadow-lg ${color} text-white`}
    >
      <div className="flex justify-between items-center mb-4">
        <h3 className="text-xl font-bold">{title}</h3>
        <span className="text-2xl">{icon}</span>
      </div>
      <p className="text-sm opacity-90">{description}</p>
    </Link>
  );
};

const Dashboard = () => {
  const [userName, setUserName] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Obtener nombre del usuario desde el almacenamiento local o API
    const storedUser = JSON.parse(sessionStorage.getItem('user')) || {};
    const firstName = storedUser.firstName || '';
    setUserName(firstName);
    setLoading(false);
  }, []);

  if (loading) {
    return <div>Cargando...</div>;
  }

  return (
    <div className="min-h-screen bg-[var(--sand)]">
      <NavigationBar title="Dashboard" />
      
      <div className="container mx-auto py-8 px-4">
        <header className="mb-8">
          <h1 className="text-3xl font-bold text-[var(--deep-sea)]">
            {userName ? `¡Hola, ${userName}!` : '¡Bienvenido!'}
          </h1>
          <p className="text-[var(--open-sea)] mt-2">
            Tu centro de conocimiento y colaboración
          </p>
        </header>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {/* Widget del perfil */}
          <DashboardWidget 
            title="Mi Perfil" 
            icon="👤" 
            description="Gestiona tu información personal, intereses y conexiones" 
            linkTo="/profile" 
            color="bg-[var(--coastal-sea)]"
          />
          
          {/* Widget de usuarios */}
          <DashboardWidget 
            title="Gestión de Usuarios" 
            icon="👥" 
            description="Explora usuarios, establece conexiones y amplía tu red académica" 
            linkTo="/users-dashboard" 
            color="bg-[var(--open-sea)]"
          />
          
          {/* Widget de contenido */}
          <DashboardWidget 
            title="Contenido" 
            icon="📚" 
            description="Administra tus documentos, recursos académicos y materiales" 
            linkTo="/content-dashboard" 
            color="bg-[var(--deep-sea)]"
          />
          
          {/* Widget de grafo de afinidad */}
          <DashboardWidget 
            title="Grafo de Afinidad" 
            icon="🔗" 
            description="Visualiza conexiones entre usuarios y áreas de conocimiento" 
            linkTo="/affinity-graph" 
            color="bg-[var(--coastal-sea)]"
          />
          
          {/* Widget de chat */}
          <DashboardWidget 
            title="Mensajes" 
            icon="💬" 
            description="Comunícate con otros usuarios y participa en conversaciones académicas" 
            linkTo="/chat" 
            color="bg-[var(--open-sea)]"
          />
          
          {/* Widget de solicitud de ayuda */}
          <DashboardWidget 
            title="Solicitar Ayuda" 
            icon="🆘" 
            description="Crea una solicitud para recibir apoyo académico de otros usuarios" 
            linkTo="/help-request" 
            color="bg-[var(--deep-sea)]"
          />
        </div>
        
        <div className="mt-10">
          <h2 className="text-xl font-semibold text-[var(--deep-sea)] mb-4">Actividad Reciente</h2>
          <div className="bg-white rounded-lg shadow p-6">
            <p className="text-[var(--open-sea)] text-center py-4">
              No hay actividad reciente para mostrar.
            </p>
            {/* Aquí se mostrará la actividad reciente cuando se implemente */}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard; 