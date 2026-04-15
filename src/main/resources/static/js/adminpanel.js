// Esperar a que el DOM se cargue completamente
document.addEventListener('DOMContentLoaded', function() {
    // Función para cambiar entre secciones del panel principal
    function setupMainNavigation() {
        const menuItems = document.querySelectorAll('.sidebar .menu ul li');
        const contentSections = document.querySelectorAll('.content-section');
        
        menuItems.forEach(item => {
            item.addEventListener('click', function() {
                // Eliminar la clase active de todos los elementos del menú
                menuItems.forEach(menuItem => menuItem.classList.remove('active'));
                
                // Añadir la clase active al elemento seleccionado
                this.classList.add('active');
                
                // Obtener la sección a mostrar
                const sectionToShow = this.getAttribute('data-section');
                
                // Ocultar todas las secciones
                contentSections.forEach(section => section.classList.remove('active'));
                
                // Mostrar la sección seleccionada
                document.getElementById(sectionToShow)?.classList.add('active');
            });
        });
        
        // Inicializar con la sección dashboard activa
        document.getElementById('dashboard')?.classList.add('active');
    }
    
    // Función para cambiar entre paneles de configuración
    function setupSettingsNavigation() {
        const settingsMenuItems = document.querySelectorAll('.settings-menu li');
        const settingsPanels = document.querySelectorAll('.settings-panel');
        
        settingsMenuItems.forEach(item => {
            item.addEventListener('click', function() {
                // Eliminar la clase active de todos los elementos del menú
                settingsMenuItems.forEach(menuItem => menuItem.classList.remove('active'));
                
                // Añadir la clase active al elemento seleccionado
                this.classList.add('active');
                
                // Obtener el panel a mostrar
                const panelToShow = this.getAttribute('data-settings');
                
                // Ocultar todos los paneles
                settingsPanels.forEach(panel => panel.classList.remove('active'));
                
                // Mostrar el panel seleccionado
                document.getElementById(panelToShow)?.classList.add('active');
            });
        });
    }
    
    // Inicializar gráficos (Chart.js)
    function setupCharts() {
        // Comprobar si Chart.js está disponible
        if (typeof Chart === 'undefined') {
            console.warn('Chart.js no está cargado');
            return;
        }

        // Configurar colores consistentes para los gráficos
        const chartColors = {
            primary: '#4361ee',
            secondary: '#3f37c9',
            success: '#4cc9f0',
            warning: '#f72585',
            danger: '#ff4d6d',
            info: '#4895ef',
            light: '#e0e1dd',
            dark: '#212529',
            purple: '#7209b7',
            pink: '#f5a6c8',
            expense: '#f94144',
            income: '#90be6d'
        };
        
        // Gráfico de Asistencia Mensual
        const attendanceCtx = document.getElementById('attendanceChart')?.getContext('2d');
        if (attendanceCtx) {
            const attendanceChart = new Chart(attendanceCtx, {
                type: 'line',
                data: {
                    labels: ['1', '5', '10', '15', '20', '25', '30'],
                    datasets: [{
                        label: 'Asistencia',
                        data: [120, 190, 170, 195, 180, 210, 190],
                        backgroundColor: chartColors.primary,
                        borderColor: chartColors.primary,
                        borderWidth: 2,
                        tension: 0.3,
                        pointRadius: 3,
                        fill: false
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                color: 'rgba(0, 0, 0, 0.05)'
                            }
                        },
                        x: {
                            grid: {
                                display: false
                            }
                        }
                    },
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }
            });
            
            // Listener para el selector de tiempo
            document.getElementById('attendanceTimeframe')?.addEventListener('change', function() {
                // Actualizar el gráfico con nuevos datos según el periodo seleccionado
                // Ejemplo simplificado, en producción se cargarían datos reales
                let newData;
                switch(this.value) {
                    case 'week':
                        newData = [80, 120, 90, 110, 95, 105, 85];
                        break;
                    case 'year':
                        newData = [1200, 1400, 1300, 1450, 1600, 1500, 1700, 1650, 1580, 1620, 1700, 1750];
                        attendanceChart.data.labels = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
                        break;
                    default: // month
                        newData = [120, 190, 170, 195, 180, 210, 190];
                        attendanceChart.data.labels = ['1', '5', '10', '15', '20', '25', '30'];
                }
                attendanceChart.data.datasets[0].data = newData;
                attendanceChart.update();
            });
        }
        
        // Gráfico de Ingresos vs Gastos
        const revenueCtx = document.getElementById('revenueChart')?.getContext('2d');
        if (revenueCtx) {
            const revenueChart = new Chart(revenueCtx, {
                type: 'bar',
                data: {
                    labels: ['Semana 1', 'Semana 2', 'Semana 3', 'Semana 4'],
                    datasets: [
                        {
                            label: 'Ingresos',
                            data: [4200, 3800, 5100, 4500],
                            backgroundColor: chartColors.income,
                            borderColor: chartColors.income,
                            borderWidth: 1
                        },
                        {
                            label: 'Gastos',
                            data: [3200, 3000, 3400, 3200],
                            backgroundColor: chartColors.expense,
                            borderColor: chartColors.expense,
                            borderWidth: 1
                        }
                    ]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                color: 'rgba(0, 0, 0, 0.05)'
                            }
                        },
                        x: {
                            grid: {
                                display: false
                            }
                        }
                    }
                }
            });
            
            // Listener para el selector de tiempo
            document.getElementById('revenueTimeframe')?.addEventListener('change', function() {
                // Similar a la función anterior, actualizaría con datos según el periodo
                let incomeData, expenseData, newLabels;
                switch(this.value) {
                    case 'quarter':
                        incomeData = [12000, 13500, 14200];
                        expenseData = [9500, 10200, 9800];
                        newLabels = ['Mes 1', 'Mes 2', 'Mes 3'];
                        break;
                    case 'year':
                        incomeData = [10000, 11000, 12000, 13000, 12500, 14000, 15000, 14500, 15500, 16000, 16500, 17000];
                        expenseData = [8000, 8500, 9000, 9200, 9500, 10000, 10500, 10800, 11000, 11500, 12000, 12500];
                        newLabels = ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic'];
                        break;
                    default: // month
                        incomeData = [4200, 3800, 5100, 4500];
                        expenseData = [3200, 3000, 3400, 3200];
                        newLabels = ['Semana 1', 'Semana 2', 'Semana 3', 'Semana 4'];
                }
                revenueChart.data.labels = newLabels;
                revenueChart.data.datasets[0].data = incomeData;
                revenueChart.data.datasets[1].data = expenseData;
                revenueChart.update();
            });
        }
        
        // Gráfico de Distribución de Membresías
        const membershipCtx = document.getElementById('membershipChart')?.getContext('2d');
        if (membershipCtx) {
            new Chart(membershipCtx, {
                type: 'pie',
                data: {
                    labels: ['Premium', 'Estándar', 'Básico'],
                    datasets: [{
                        data: [42, 35, 23],
                        backgroundColor: [
                            chartColors.primary,
                            chartColors.info,
                            chartColors.warning
                        ],
                        borderColor: '#ffffff',
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: false
                        }
                    }
                }
            });
        }
        
        // Gráfico de Factores de Cancelación (para sección de predicciones)
        const cancellationFactorsCtx = document.getElementById('cancellationFactorsChart')?.getContext('2d');
        if (cancellationFactorsCtx) {
            new Chart(cancellationFactorsCtx, {
                type: 'bar',
                data: {
                    labels: ['Precio', 'Falta de tiempo', 'Poco uso', 'Mal servicio', 'Mudanza', 'Otros'],
                    datasets: [{
                        label: 'Factores de Cancelación',
                        data: [25, 20, 30, 10, 12, 3],
                        backgroundColor: chartColors.warning,
                        borderColor: chartColors.warning,
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    indexAxis: 'y',
                    scales: {
                        x: {
                            beginAtZero: true,
                            grid: {
                                color: 'rgba(0, 0, 0, 0.05)'
                            }
                        },
                        y: {
                            grid: {
                                display: false
                            }
                        }
                    }
                }
            });
        }
        
        // Gráfico de Tendencia de Cancelaciones
        const cancellationTrendCtx = document.getElementById('cancellationTrendChart')?.getContext('2d');
        if (cancellationTrendCtx) {
            new Chart(cancellationTrendCtx, {
                type: 'line',
                data: {
                    labels: ['Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun'],
                    datasets: [{
                        label: 'Cancelaciones',
                        data: [8, 12, 9, 14, 10, 12],
                        backgroundColor: chartColors.danger,
                        borderColor: chartColors.danger,
                        borderWidth: 2,
                        tension: 0.3,
                        pointRadius: 3,
                        fill: false
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        y: {
                            beginAtZero: true,
                            grid: {
                                color: 'rgba(0, 0, 0, 0.05)'
                            }
                        },
                        x: {
                            grid: {
                                display: false
                            }
                        }
                    }
                }
            });
        }
    }
    
    // Configurar funcionalidad para la tabla de usuarios
    function setupUsersTable() {
        // Select all checkbox functionality
        const selectAllCheckbox = document.getElementById('selectAll');
        const userCheckboxes = document.querySelectorAll('.user-checkbox');
        
        if (selectAllCheckbox) {
            selectAllCheckbox.addEventListener('change', function() {
                userCheckboxes.forEach(checkbox => {
                    checkbox.checked = this.checked;
                });
            });
        }
        
        // Filtros de usuarios
        const membershipFilter = document.getElementById('membershipFilter');
        const statusFilter = document.getElementById('statusFilter');
        const userRows = document.querySelectorAll('.users-table tbody tr');
        
        function applyFilters() {
            const membershipValue = membershipFilter?.value || 'all';
            const statusValue = statusFilter?.value || 'all';
            
            userRows.forEach(row => {
                const membershipType = row.querySelector('.badge').textContent.toLowerCase();
                const statusType = row.querySelector('.status').textContent.toLowerCase();
                
                const matchMembership = membershipValue === 'all' || membershipType.includes(membershipValue);
                const matchStatus = statusValue === 'all' || statusType.includes(statusValue);
                
                if (matchMembership && matchStatus) {
                    row.style.display = '';
                } else {
                    row.style.display = 'none';
                }
            });
        }
        
        membershipFilter?.addEventListener('change', applyFilters);
        statusFilter?.addEventListener('change', applyFilters);
    }
    
    // Configurar funcionalidad para promociones
    function setupPromotions() {
        // Filtros de promociones
        const promotionStatusFilter = document.getElementById('promotionStatusFilter');
        const promotionTypeFilter = document.getElementById('promotionTypeFilter');
        const promotionCards = document.querySelectorAll('.promotion-card');
        
        function applyPromotionFilters() {
            const statusValue = promotionStatusFilter?.value || 'all';
            const typeValue = promotionTypeFilter?.value || 'all';
            
            promotionCards.forEach(card => {
                const statusType = card.classList.contains('active') ? 'active' : 
                                  card.classList.contains('scheduled') ? 'scheduled' : 'expired';
                                  
                // Determinar el tipo de promoción basado en el icono
                const iconElement = card.querySelector('.promo-icon i');
                const iconClass = iconElement ? iconElement.className : '';
                let promoType = 'discount'; // valor por defecto
                
                if (iconClass.includes('gift')) promoType = 'gift';
                else if (iconClass.includes('users') || iconClass.includes('stopwatch')) promoType = 'free';
                
                const matchStatus = statusValue === 'all' || statusType === statusValue;
                const matchType = typeValue === 'all' || promoType === typeValue;
                
                if (matchStatus && matchType) {
                    card.style.display = '';
                } else {
                    card.style.display = 'none';
                }
            });
        }
        
        promotionStatusFilter?.addEventListener('change', applyPromotionFilters);
        promotionTypeFilter?.addEventListener('change', applyPromotionFilters);
    }
    
    // Inicializar todas las funcionalidades
    setupMainNavigation();
    setupSettingsNavigation();
    setupCharts();
    setupUsersTable();
    setupPromotions();
    
    // Funcionalidad para inputs de búsqueda
    const searchInput = document.querySelector('.search-container input');
    if (searchInput) {
        searchInput.addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            // Aquí implementarías la lógica de búsqueda según la sección activa
            console.log('Buscando:', searchTerm);
        });
    }
    
    // Manejar envío de formularios para prevenir recarga de página
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            // Aquí implementarías la lógica para manejar el envío del formulario
            console.log('Formulario enviado');
        });
    });
    
    // Funcionalidad para notificaciones y mensajes en la barra superior
    const notificationIcon = document.querySelector('.notification i');
    const messageIcon = document.querySelector('.message i');
    
    if (notificationIcon) {
        notificationIcon.addEventListener('click', function() {
            alert('Notificaciones: 5 nuevas notificaciones');
        });
    }
    
    if (messageIcon) {
        messageIcon.addEventListener('click', function() {
            alert('Mensajes: 3 nuevos mensajes');
        });
    }
});