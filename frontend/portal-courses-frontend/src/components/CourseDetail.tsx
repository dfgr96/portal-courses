import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import type { Course } from "../models/Course";

export default function CourseDetail() {
  const { id } = useParams();
  const [course, setCourse] = useState<Course | null>(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchCourse = async () => {
      try {
        const token = localStorage.getItem("token");
        console.log("Usando token:", token, "para courseId:", id);

        const response = await fetch(`http://localhost:8080/courses/${id}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        console.log("Respuesta HTTP:", response.status);

        if (!response.ok) {
          throw new Error("Error al obtener curso");
        }

        const data: Course = await response.json();
        console.log("Datos del curso:", data);
        setCourse(data);
      } catch (err: any) {
        setError(err.message);
      }
    };

    fetchCourse();
  }, [id]);

  if (error) return <p style={{ color: "red" }}>{error}</p>;
  if (!course) return <p>Cargando curso...</p>;

  return (
    <div style={{ maxWidth: 600, margin: "2rem auto" }}>
      <h2>{course.title}</h2>
      <p>{course.description}</p>

      <h3>Módulos</h3>
      <ul>
        {course.modules.map((m) => (
          <li key={m.id}>
            <strong>{m.title}</strong> ({m.contentType})
          </li>
        ))}
      </ul>
    </div>
  );
}
