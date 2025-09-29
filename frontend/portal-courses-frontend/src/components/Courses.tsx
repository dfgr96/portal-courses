import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

function Courses() {
  const [courses, setCourses] = useState<any[]>([]);

  useEffect(() => {
    const fetchCourses = async () => {
      const token = localStorage.getItem("token");
      const response = await fetch("http://localhost:8080/courses", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      const data = await response.json();
      setCourses(data);
    };
    fetchCourses();
  }, []);

  return (
    <div style={{ maxWidth: 600, margin: "2rem auto" }}>
      <h2>Mis cursos</h2>
      <ul>
        {courses.map((course) => (
          <li key={course.id}>
            <Link to={`/courses/${course.id}`}>
              <b>{course.title}</b>
            </Link>{" "}
            - {course.description}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default Courses;