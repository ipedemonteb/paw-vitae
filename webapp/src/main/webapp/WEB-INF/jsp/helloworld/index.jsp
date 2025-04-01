<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MedHub - Reserva de Turnos Médicos</title>
    <!-- Enlace al archivo CSS externo -->
    <link rel="stylesheet" href="<c:url value='/css/styles.css' />">
</head>
<body>
<header>
    <div class="container">
        <div class="logo">
            <div class="logo-circle">M</div>
            <span>MedHub</span>
        </div>
        <nav>
            <ul>
                <li><a href="#como-funciona">Cómo funciona</a></li>
                <li><a href="#especialidades">Especialidades</a></li>
                <li><a href="#opiniones">Opiniones</a></li>
                <li><a href="#ayuda">Ayuda</a></li>
            </ul>
        </nav>
        <div class="auth-buttons">
            <button class="btn-outline">Iniciar sesión</button>
            <button class="btn-primary">Registrarse</button>
        </div>
        <div class="menu-toggle">
            <span></span>
            <span></span>
            <span></span>
        </div>
    </div>
</header>

<main>
    <!-- Hero Section -->
    <section class="hero">
        <div class="container">
            <div class="hero-content">
                <h1>Reserva tu turno médico en minutos</h1>
                <p>MedHub te conecta con los mejores profesionales de la salud. Encuentra especialistas y agenda tu cita de forma rápida y sencilla.</p>
                <div class="hero-buttons">
                    <button class="btn-primary">Buscar especialista</button>
                    <button class="btn-outline">Ver especialidades</button>
                </div>
            </div>
            <div class="hero-image">
                <img src="<c:url value='/' />" alt="Paciente reservando turno en MedHub">
            </div>
        </div>
    </section>

    <!-- How it Works Section -->
    <section class="how-it-works" id="como-funciona">
        <div class="container">
            <div class="section-header">
                <span class="badge">Fácil y rápido</span>
                <h2>Reserva tu turno en 3 simples pasos</h2>
                <p>MedHub hace que conseguir una cita médica sea tan sencillo como ordenar comida online.</p>
            </div>
            <div class="steps">
                <div class="step-card">
                    <div class="step-icon">
                        <i class="icon-search"></i>
                    </div>
                    <h3>Busca tu especialista</h3>
                    <p>Encuentra el médico ideal por especialidad, ubicación o disponibilidad.</p>
                </div>
                <div class="step-card">
                    <div class="step-icon">
                        <i class="icon-calendar"></i>
                    </div>
                    <h3>Selecciona fecha y hora</h3>
                    <p>Elige el horario que mejor se adapte a tu agenda entre las opciones disponibles.</p>
                </div>
                <div class="step-card">
                    <div class="step-icon">
                        <i class="icon-bell"></i>
                    </div>
                    <h3>Recibe confirmación</h3>
                    <p>Obtén confirmación instantánea y recordatorios previos a tu cita.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Specialties Section -->
    <section class="specialties" id="especialidades">
        <div class="container">
            <div class="specialties-content">
                <h2>Encuentra especialistas en todas las áreas</h2>
                <p>MedHub te conecta con profesionales de todas las especialidades médicas.</p>
                <div class="specialty-grid">
                    <%-- Usando JSP para generar especialidades dinámicamente --%>
                    <% String[] especialidades = {"Cardiología", "Dermatología", "Pediatría", "Ginecología", "Traumatología", "Oftalmología"}; %>
                    <% for(String especialidad : especialidades) { %>
                    <div class="specialty-item">
                        <div class="specialty-dot"></div>
                        <span><%= especialidad %></span>
                    </div>
                    <% } %>
                </div>
                <a href="#" class="link">Ver todas las especialidades →</a>
            </div>
            <div class="specialties-image">
                <img src="<c:url value='/' />" alt="Especialidades médicas en MedHub">
            </div>
        </div>
    </section>

    <!-- Benefits Section -->
    <section class="benefits">
        <div class="container">
            <div class="section-header">
                <h2>Beneficios para pacientes</h2>
                <p>Descubre por qué miles de pacientes eligen MedHub para gestionar sus citas médicas.</p>
            </div>
            <div class="benefits-grid">
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="icon-clock"></i>
                    </div>
                    <h3>Ahorra tiempo</h3>
                    <p>Reserva turnos 24/7 sin llamadas ni esperas.</p>
                </div>
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="icon-search"></i>
                    </div>
                    <h3>Amplia red médica</h3>
                    <p>Accede a miles de especialistas en un solo lugar.</p>
                </div>
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="icon-bell"></i>
                    </div>
                    <h3>Recordatorios</h3>
                    <p>Recibe alertas para no olvidar tus citas.</p>
                </div>
                <div class="benefit-card">
                    <div class="benefit-icon">
                        <i class="icon-calendar"></i>
                    </div>
                    <h3>Historial de citas</h3>
                    <p>Mantén un registro de todas tus consultas médicas.</p>
                </div>
            </div>
        </div>
    </section>

    <!-- Testimonials Section -->
    <section class="testimonials" id="opiniones">
        <div class="container">
            <div class="section-header">
                <h2>Lo que dicen nuestros usuarios</h2>
                <p>Miles de pacientes ya confían en MedHub para gestionar sus citas médicas.</p>
            </div>
            <div class="testimonials-grid">
                <%-- Estructura de datos para testimonios usando JSP --%>
                <%
                    String[][] testimonios = {
                            {"Laura Martínez", "Paciente", "Conseguir un turno con mi médico nunca había sido tan fácil. La aplicación es intuitiva y me ahorra muchísimo tiempo.", "LM"},
                            {"Carlos Rodríguez", "Paciente", "Los recordatorios son geniales. Desde que uso MedHub no he vuelto a olvidar ninguna cita médica.", "CR"},
                            {"Ana García", "Paciente", "Poder ver la disponibilidad de los médicos en tiempo real y elegir el horario que me conviene es fantástico.", "AG"}
                    };
                %>

                <% for(String[] testimonio : testimonios) { %>
                <div class="testimonial-card">
                    <div class="stars">
                        <i class="icon-star"></i>
                        <i class="icon-star"></i>
                        <i class="icon-star"></i>
                        <i class="icon-star"></i>
                        <i class="icon-star"></i>
                    </div>
                    <p>"<%= testimonio[2] %>"</p>
                    <div class="testimonial-author">
                        <div class="author-avatar"><%= testimonio[3] %></div>
                        <div class="author-info">
                            <h4><%= testimonio[0] %></h4>
                            <span><%= testimonio[1] %></span>
                        </div>
                    </div>
                </div>
                <% } %>
            </div>
        </div>
    </section>

    <!-- App Download Section -->
    <section class="app-download">
        <div class="container">
            <div class="app-content">
                <h2>Descarga la app y lleva tus citas médicas en tu bolsillo</h2>
                <p>Gestiona tus citas médicas desde cualquier lugar y en cualquier momento.</p>
                <div class="app-buttons">
                    <button class="btn-app">
                        <i class="icon-apple"></i>
                        <div>
                            <small>Descargar en</small>
                            <span>App Store</span>
                        </div>
                    </button>
                    <button class="btn-app">
                        <i class="icon-android"></i>
                        <div>
                            <small>Disponible en</small>
                            <span>Google Play</span>
                        </div>
                    </button>
                </div>
            </div>
            <div class="app-image">
                <img src="<c:url value='' />" alt="App móvil de MedHub">
            </div>
        </div>
    </section>

    <!-- CTA Section -->
    <section class="cta">
        <div class="container">
            <h2>¿Listo para simplificar tus citas médicas?</h2>
            <p>Únete a miles de pacientes que ya disfrutan de la forma más fácil de reservar turnos médicos.</p>
            <form class="cta-form" action="<c:url value='/' />" method="post">
                <input type="email" name="email" placeholder="Ingresa tu email" required>
                <button type="submit" class="btn-secondary">Registrarse</button>
            </form>
        </div>
    </section>
</main>

<footer>
    <div class="container">
        <div class="footer-logo">
            <div class="logo-circle">M</div>
            <span>MedHub</span>
        </div>
        <p class="copyright">&copy; <%= new SimpleDateFormat("yyyy").format(new Date()) %> MedHub. Todos los derechos reservados.</p>
        <nav class="footer-nav">
            <a href="#">Términos de servicio</a>
            <a href="#">Privacidad</a>
            <a href="#">Cookies</a>
        </nav>
    </div>
</footer>


</body>
</html>