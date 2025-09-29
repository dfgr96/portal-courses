import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import type { Course } from "../models/Course";

export default function CourseList() {
  const [courses, setCourses] = useState<Course[]>([]);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        const token = localStorage.getItem("token");
        const res = await fetch("http://localhost:8080/courses", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!res.ok) throw new Error("Error al obtener cursos");

        const data: Course[] = await res.json();
        setCourses(data);
      } catch (err: any) {
        setError(err.message);
      }
    };

    fetchCourses();
  }, []);

  if (error) return <p style={{ color: "red" }}>{error}</p>;

  return (
    <div>
      <h2>Lista de Cursos</h2>
      <ul>
        {courses.map((c) => (
          <li key={c.id}>
            {/* 👇 Link hacia CourseDetail */}
            <Link to={`/courses/${c.id}`}>{c.title}</Link>
          </li>
        ))}
      </ul>
    </div>
  );
}
